ALTER TABLE Recording
    DROP COLUMN recording;

ALTER TABLE Recording
    ADD recording BLOB NULL;