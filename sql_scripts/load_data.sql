/* LOAD DATA LOCAL INFILE '~/Downloads/archive-2/data-total.csv' */
/* INTO TABLE GoodReadsChoiceAwards */
/* FIELDS TERMINATED BY ',' */
/* ENCLOSED BY '"' */
/* LINES TERMINATED BY '\n' */
/* IGNORE 1 ROWS; */

LOAD DATA LOCAL INFILE '~/Downloads/GoodReads_100k_books.csv'
INTO TABLE GoodReads100K
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;
