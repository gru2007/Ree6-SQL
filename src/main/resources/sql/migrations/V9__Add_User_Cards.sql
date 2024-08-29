CREATE TABLE UserRankCard
(
    userId   BIGINT NOT NULL,
    rankCard BLOB NULL,
    CONSTRAINT pk_userrankcard PRIMARY KEY (userId)
);

ALTER TABLE Tickets
    ALTER ticketCount SET DEFAULT 0;