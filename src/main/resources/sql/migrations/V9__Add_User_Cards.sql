CREATE TABLE UserRankCard
(
    userId   BIGINT NOT NULL,
    rankCard MEDIUMBLOB NULL,
    CONSTRAINT pk_userrankcard PRIMARY KEY (userId)
);

ALTER TABLE Tickets
    ALTER ticketCount SET DEFAULT 0;