CREATE TABLE Profile (
    PId MEDIUMINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    PName VARCHAR(60) UNIQUE NOT NULL
);

CREATE TABLE Course (
    CId MEDIUMINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    CProfileId MEDIUMINT NOT NULL,
    CName VARCHAR(60) NOT NULL,

    AplusGrade TINYINT NOT NULL,
    AGrade TINYINT NOT NULL,
    AminusGrade TINYINT NOT NULL,
    BplusGrade TINYINT NOT NULL,
    BGrade TINYINT NOT NULL,
    BminusGrade TINYINT NOT NULL,
    CplusGrade TINYINT NOT NULL,
    CGrade TINYINT NOT NULL,
    DGrade TINYINT NOT NULL,

    FOREIGN KEY (CProfileId) REFERENCES Profile(PId),
    CHECK (AplusGrade > AGrade),
    CHECK (AGrade > AminusGrade),
    CHECK (AminusGrade > BplusGrade),
    CHECK (BplusGrade > BGrade),
    CHECK (BGrade > BminusGrade),
    CHECK (BminusGrade > CplusGrade),
    CHECK (CplusGrade > CGrade),
    CHECK (CGrade > DGrade)
);

CREATE TABLE CourseCategory (
    CatId MEDIUMINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    CatCourseId MEDIUMINT NOT NULL,
    CatName VARCHAR(60) NOT NULL,
    Percentage TINYINT NOT NULL,

    FOREIGN KEY (CatCourseId) REFERENCES Course(CId),
    CHECK (Percentage > 0 AND Percentage <= 100)
);
