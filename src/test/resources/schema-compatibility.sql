-- Table definitions copied from database/CommunityStoreDB.sql; procedures/triggers are MySQL-only.
CREATE TABLE Roles (
    roleId INT AUTO_INCREMENT PRIMARY KEY,
    roleName VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE Users (
    userId INT AUTO_INCREMENT PRIMARY KEY,
    roleId INT NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    passwordHash VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(20),
    studentNumber VARCHAR(20) UNIQUE,
    dateCreated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'Active',
    CONSTRAINT fk_users_roles FOREIGN KEY (roleId) REFERENCES Roles(roleId)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT ck_users_status CHECK (status IN ('Active','Inactive','Suspended'))
);

CREATE TABLE VendorProfiles (
    vendorProfileId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL UNIQUE,
    businessName VARCHAR(150) NOT NULL,
    businessRegistrationNumber VARCHAR(100) NOT NULL UNIQUE,
    verificationStatus VARCHAR(20) NOT NULL DEFAULT 'Pending',
    verificationDocumentUrl VARCHAR(255) NOT NULL,
    submittedDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    verifiedDate DATETIME,
    CONSTRAINT fk_vendorProfiles_user FOREIGN KEY (userId) REFERENCES Users(userId)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT ck_vendorProfiles_status CHECK (verificationStatus IN ('Pending','Verified','Rejected'))
);

CREATE TABLE Categories (
    categoryId INT AUTO_INCREMENT PRIMARY KEY,
    categoryName VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE Products (
    productId INT AUTO_INCREMENT PRIMARY KEY,
    sellerId INT NOT NULL,
    categoryId INT NOT NULL,
    productName VARCHAR(150) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    `condition` VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'Available',
    datePosted DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_seller FOREIGN KEY (sellerId) REFERENCES Users(userId)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_products_category FOREIGN KEY (categoryId) REFERENCES Categories(categoryId)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT ck_products_price CHECK (price >= 0),
    CONSTRAINT ck_products_quantity CHECK (quantity >= 0),
    CONSTRAINT ck_products_status CHECK (status IN ('Available','Sold','Inactive','Pending')),
    CONSTRAINT ck_products_condition CHECK (`condition` IN ('New','Like New','Good','Fair','Poor'))
);

CREATE TABLE ProductImages (
    imageId INT AUTO_INCREMENT PRIMARY KEY,
    productId INT NOT NULL,
    imageUrl VARCHAR(255) NOT NULL,
    isPrimary BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_productImages_product FOREIGN KEY (productId) REFERENCES Products(productId)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE ShoppingCart (
    cartId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL UNIQUE,
    createdDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_shoppingCart_user FOREIGN KEY (userId) REFERENCES Users(userId)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE CartItems (
    cartItemId INT AUTO_INCREMENT PRIMARY KEY,
    cartId INT NOT NULL,
    productId INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    CONSTRAINT fk_cartItems_cart FOREIGN KEY (cartId) REFERENCES ShoppingCart(cartId)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_cartItems_product FOREIGN KEY (productId) REFERENCES Products(productId)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT uq_cart_product UNIQUE (cartId, productId),
    CONSTRAINT ck_cartItems_quantity CHECK (quantity > 0)
);

CREATE TABLE Orders (
    orderId INT AUTO_INCREMENT PRIMARY KEY,
    buyerId INT NOT NULL,
    orderDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    orderStatus VARCHAR(30) NOT NULL DEFAULT 'Pending',
    totalAmount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    shippingAddress VARCHAR(255) NOT NULL,
    CONSTRAINT fk_orders_buyer FOREIGN KEY (buyerId) REFERENCES Users(userId)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT ck_orders_total CHECK (totalAmount >= 0),
    CONSTRAINT ck_orders_status CHECK (orderStatus IN
        ('Pending','Paid','Processing','Shipped','Completed','Cancelled'))
);

CREATE TABLE OrderItems (
    orderItemId INT AUTO_INCREMENT PRIMARY KEY,
    orderId INT NOT NULL,
    productId INT NOT NULL,
    quantity INT NOT NULL,
    unitPrice DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_orderItems_order FOREIGN KEY (orderId) REFERENCES Orders(orderId)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_orderItems_product FOREIGN KEY (productId) REFERENCES Products(productId)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT ck_orderItems_quantity CHECK (quantity > 0),
    CONSTRAINT ck_orderItems_unitPrice CHECK (unitPrice >= 0)
);

CREATE TABLE Payments (
    paymentId INT AUTO_INCREMENT PRIMARY KEY,
    orderId INT NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL,
    paymentMethod VARCHAR(50) NOT NULL,
    paymentStatus VARCHAR(30) NOT NULL DEFAULT 'Pending',
    paymentDate DATETIME,
    transactionReference VARCHAR(100) UNIQUE,
    CONSTRAINT fk_payments_order FOREIGN KEY (orderId) REFERENCES Orders(orderId)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT ck_payments_amount CHECK (amount >= 0),
    CONSTRAINT ck_payments_status CHECK
        (paymentStatus IN ('Pending','Successful','Failed','Refunded'))
);

CREATE TABLE Reviews (
    reviewId INT AUTO_INCREMENT PRIMARY KEY,
    productId INT NOT NULL,
    reviewerId INT NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    reviewDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reviews_product FOREIGN KEY (productId) REFERENCES Products(productId)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_reviews_reviewer FOREIGN KEY (reviewerId) REFERENCES Users(userId)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT uq_review_user_product UNIQUE (productId, reviewerId),
    CONSTRAINT ck_reviews_rating CHECK (rating BETWEEN 1 AND 5)
);

CREATE TABLE CommunityPosts (
    postId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    content TEXT NOT NULL,
    postDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_communityPosts_user FOREIGN KEY (userId) REFERENCES Users(userId)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Notifications (
    notificationId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL,
    message TEXT NOT NULL,
    isRead BOOLEAN NOT NULL DEFAULT FALSE,
    createdDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notifications_user FOREIGN KEY (userId) REFERENCES Users(userId)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Reports (
    reportId INT AUTO_INCREMENT PRIMARY KEY,
    reporterId INT NOT NULL,
    reviewedBy INT,
    targetType VARCHAR(30) NOT NULL,
    targetId INT NOT NULL,
    reason VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'Pending',
    createdDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolvedDate DATETIME,
    CONSTRAINT fk_reports_reporter FOREIGN KEY (reporterId) REFERENCES Users(userId)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_reports_reviewedBy FOREIGN KEY (reviewedBy) REFERENCES Users(userId)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT ck_reports_targetType CHECK (targetType IN ('Product','Review','CommunityPost','User')),
    CONSTRAINT ck_reports_status CHECK (status IN ('Pending','Reviewed','ActionTaken','Dismissed'))
);
