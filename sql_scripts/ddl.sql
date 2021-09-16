DROP TABLE IF EXISTS GoodReadsChoiceAwards;

CREATE TABLE GoodReadsChoiceAwards (
    title varchar(500),
    author varchar(500),
    description varchar(2000),
    bookformat varchar(500),
    bookedition varchar(500),
    pages float,
    published_date date,
    publisher varchar(500),
    reading_age varchar(500),
    lexile_measure varchar(500),
    grade_level varchar(500),
    weight float,
    rating_value0 float,
    rating_value1 float,
    rating_count0 float,
    rating_count1 float,
    dimension_0 float,
    dimension_1 float,
    genre_0 varchar(500),
    genre_1 varchar(500),
    genre_2 varchar(500),
    genre_3 varchar(500),
    genre_4 varchar(500),
    genre_5 varchar(500),
    genre_6 varchar(500),
    genre_7 varchar(500),
    genre_8 varchar(500),
    genre_9 varchar(500),
    genere_0_weight float,
    genere_1_weight float,
    genere_2_weight float,
    genere_3_weight float,
    genere_4_weight float,
    genere_5_weight float,
    genere_6_weight float,
    genere_7_weight float,
    genere_8_weight float,
    genere_9_weight float,
    price float
);


DROP TABLE IF EXISTS GoodReads100K;

CREATE TABLE GoodReads100K (
    author varchar(500),
    bookformat varchar(500),
    description varchar(2000),
    genre varchar(500),
    img varchar(500),
    isbn varchar(500),
    isnb13 varchar(500),
    link varchar(500),
    pages int,
    rating float,
    reviews int,
    title varchar(500),
    totalratings int
);
