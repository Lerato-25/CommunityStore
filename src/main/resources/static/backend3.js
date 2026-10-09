"use strict";
const $ = id => document.getElementById(id);
let authorization = "", session = null, editId = null;
async function api(path, method="GET", body) {
  const headers = {Authorization: authorization};
  if (body !== undefined) headers["Content-Type"] = "application/json";
  if (method !== "GET" && session) headers[session.csrfHeader] = session.csrfToken;
  const response = await fetch(path, {method, headers, body: body === undefined ? undefined : JSON.stringify(body)});
  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error.message || error.detail || `Request failed (${response.status}).`);
  }
  return response.status === 204 ? null : response.json();
}
function message(text, error=false) { $("status").textContent=text; $("status").className=error ? "error" : ""; }
async function run(action) {
  const buttons=[...document.querySelectorAll("button")]; buttons.forEach(b=>b.disabled=true);
  try { await action(); } catch (error) { message(error.message,true); }
  finally { buttons.forEach(b=>b.disabled=false); $("payment").querySelector("button").disabled=!$("order").options.length; }
}
function item(parent, title, text) {
  const node=document.createElement("article"); node.className="item";
  const heading=document.createElement("strong"); heading.textContent=title; node.append(heading);
  const p=document.createElement("p"); p.textContent=text; node.append(p); $(parent).append(node); return node;
}
function button(node,text,action) { const b=document.createElement("button");b.type="button";b.textContent=text;b.className="secondary";b.onclick=()=>run(action);node.append(b); }
async function loadPayments() {
  const [orders,payments]=await Promise.all([api("/api/backend3/orders"),api("/api/payments")]);
  $("order").replaceChildren();orders.filter(o=>o.status==="Pending"&&!payments.some(p=>p.orderId===o.orderId)).forEach(o=>{
    const option=document.createElement("option");option.value=o.orderId;option.dataset.amount=o.amount;option.textContent=`Order #${o.orderId} — R${Number(o.amount).toFixed(2)}`;$("order").append(option);
  });
  $("payment").querySelector("button").disabled=!$("order").options.length;
  $("payments").replaceChildren();if(!payments.length)item("payments","No payments yet","Create a pending payment above.");
  payments.forEach(p=>{const node=item("payments",`Payment #${p.paymentId} · ${p.paymentStatus}`,`Order #${p.orderId} · R${Number(p.amount).toFixed(2)} · ${p.transactionReference||"Awaiting demo result"}`);
    if(p.paymentStatus==="Pending")for(const successful of [true,false])button(node,successful ? "Simulate success" : "Simulate failure",async()=>{await api(`/api/payments/${p.paymentId}/demo-result`,"POST",{successful});await Promise.all([loadPayments(),loadNotifications()]);message("Demo result saved. No money was charged.");});
  });
}
async function loadNotifications() {
  const rows=await api(`/api/notifications?unreadOnly=${$("unread").checked}`);$("notifications").replaceChildren();
  if(!rows.length)item("notifications","All caught up","No notifications in this view.");
  rows.forEach(n=>{const node=item("notifications",n.isRead ? "Read" : "Unread",n.message);if(!n.isRead)button(node,"Mark read",async()=>{await api(`/api/notifications/${n.notificationId}/read`,"PATCH");await loadNotifications();});});
}
async function loadPosts() {
  const rows=await api("/api/community-posts");$("posts").replaceChildren();
  if(!rows.length)item("posts","Start a conversation","Publish the first community post.");
  rows.forEach(p=>{const node=item("posts",`#${p.postId} · ${p.title}`,`${p.content}\nBy ${p.author}`);
    button(node,"Report",async()=>{$("targetType").value="CommunityPost";$("targetId").value=p.postId;$("reason").focus();});
    if(p.userId===session.userId||session.role==="Admin") {
      button(node,"Edit",async()=>{editId=p.postId;$("title").value=p.title;$("content").value=p.content;$("post").querySelector("button").textContent="Save changes";$("cancelEdit").hidden=false;$("title").focus();});
      button(node,"Delete",async()=>{if(!confirm("Delete this community post?"))return;await api(`/api/community-posts/${p.postId}`,"DELETE");await loadPosts();});
    }
  });
}
async function loadReports() {
  const rows=await api("/api/reports");$("reports").replaceChildren();if(!rows.length)item("reports","No reports","Reports you can access appear here.");
  rows.forEach(r=>{const node=item("reports",`Report #${r.reportId} · ${r.status}`,`${r.targetType} #${r.targetId} · ${r.reason}\n${r.description||""}`);
    if(session.role==="Admin"&&!['ActionTaken','Dismissed'].includes(r.status))for(const status of ['Reviewed','ActionTaken','Dismissed'])button(node,status,async()=>{await api(`/api/reports/${r.reportId}/review`,"PATCH",{status});await loadReports();});
  });
}
function cancelEdit(){editId=null;$("post").reset();$("cancelEdit").hidden=true;$("post").querySelector("button").textContent="Publish post";}
$("login").onsubmit=event=>{event.preventDefault();run(async()=>{
  authorization="Basic "+btoa(unescape(encodeURIComponent(`${$("email").value.trim()}:${$("password").value}`)));
  try { session=await api("/api/backend3/session"); } catch(error) { authorization="";session=null;$("workspace").hidden=true;throw error; }
  $("password").value="";$("identity").textContent=`Signed in as ${session.name} (${session.role})`;
  $("workspace").hidden=false;$("logout").hidden=false;cancelEdit();await Promise.all([loadPayments(),loadNotifications(),loadPosts(),loadReports()]);message("Ready.");
});};
$("logout").onclick=()=>{authorization="";session=null;$("workspace").hidden=true;$("logout").hidden=true;$("identity").textContent="";message("Signed out of the demo page.");};
$("payment").onsubmit=event=>{event.preventDefault();run(async()=>{const option=$("order").selectedOptions[0];if(!option)throw new Error("No unpaid order available.");await api("/api/payments","POST",{orderId:Number(option.value),amount:Number(option.dataset.amount),paymentMethod:"Demo"});await loadPayments();message("Pending demo payment created.");});};
$("post").onsubmit=event=>{event.preventDefault();run(async()=>{await api(editId ? `/api/community-posts/${editId}` : "/api/community-posts",editId ? "PUT" : "POST",{title:$("title").value,content:$("content").value});cancelEdit();await loadPosts();message("Post saved.");});};
$("cancelEdit").onclick=cancelEdit;
$("report").onsubmit=event=>{event.preventDefault();run(async()=>{await api("/api/reports","POST",{targetType:$("targetType").value,targetId:Number($("targetId").value),reason:$("reason").value,description:$("description").value});$("report").reset();await loadReports();message("Report submitted.");});};
$("refreshNotifications").onclick=()=>run(loadNotifications);$("unread").onchange=()=>run(loadNotifications);
