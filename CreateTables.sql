SET autocommit off;
SET pagesize 500
SET linesize 500
SET define off

CREATE TABLE Membership (
    MemberID int NOT NULL PRIMARY KEY, 
    Name varChar2(50) NOT NULL, 
    TeleNum int NOT NULL, 
    Addr varChar2(255), 
    TotalSpend int, 
    LastDate DATE, 
    Tickets int, 
    Tokens int);

CREATE TABLE Game (
    GameID int NOT NULL PRIMARY KEY, 
    GName varChar2(255), 
    TokenCost int);

CREATE TABLE GameXact (
    GXactID int NOT NULL PRIMARY KEY, 
    MemberID int NOT NULL, 
    GameID int NOT NULL, 
    Score int);

CREATE TABLE TTXact (
    TTXactID int NOT NULL PRIMARY KEY, 
    MemberID int NOT NULL, 
    Category varChar2(255), 
    Amount int NOT NULL, 
    TDate DATE);

CREATE TABLE PrizeXact (
    PXactID int NOT NULL PRIMARY KEY, 
    MemberID int NOT NULL, 
    PrizeID int NOT NULL);

CREATE TABLE Prize (
    PrizeID int NOT NULL PRIMARY KEY, 
    PName varChar2(255), 
    TicketCost int);

CREATE TABLE CouponXact (
    CXactID int NOT NULL PRIMARY KEY, 
    MemberID int NOT NULL, 
    CouponID int NOT NULL);

CREATE TABLE Coupon (
    CouponID int NOT NULL PRIMARY KEY, 
    FoodItem varchar2(255));

GRANT SELECT, DELETE, INSERT, UPDATE ON Membership TO PUBLIC;
GRANT SELECT, DELETE, INSERT, UPDATE ON Game TO PUBLIC;
GRANT SELECT, DELETE, INSERT, UPDATE ON GameXact TO PUBLIC;
GRANT SELECT, DELETE, INSERT, UPDATE ON TTXact TO PUBLIC;
GRANT SELECT, DELETE, INSERT, UPDATE ON PrizeXact TO PUBLIC;
GRANT SELECT, DELETE, INSERT, UPDATE ON Prize TO PUBLIC;
GRANT SELECT, DELETE, INSERT, UPDATE ON Coupon TO PUBLIC;
GRANT SELECT, DELETE, INSERT, UPDATE ON CouponXact TO PUBLIC;

Commit;