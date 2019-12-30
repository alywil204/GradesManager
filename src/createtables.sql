CREATE TABLE Profile (
    PId MEDIUMINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    PName VARCHAR(60) UNIQUE NOT NULL
);

CREATE TABLE Course (
    CId MEDIUMINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    CProfileId MEDIUMINT NOT NULL,
    CName VARCHAR(60) NOT NULL,

    AplusGrade FLOAT NOT NULL,
    AGrade FLOAT NOT NULL,
    AminusGrade FLOAT NOT NULL,
    BplusGrade FLOAT NOT NULL,
    BGrade FLOAT NOT NULL,
    BminusGrade FLOAT NOT NULL,
    CplusGrade FLOAT NOT NULL,
    CGrade FLOAT NOT NULL,
    DGrade FLOAT NOT NULL,

    FOREIGN KEY (CProfileId) REFERENCES Profile(PId),
    CHECK (AplusGrade <= 100),
    CHECK (AplusGrade > AGrade),
    CHECK (AGrade > AminusGrade),
    CHECK (AminusGrade > BplusGrade),
    CHECK (BplusGrade > BGrade),
    CHECK (BGrade > BminusGrade),
    CHECK (BminusGrade > CplusGrade),
    CHECK (CplusGrade > CGrade),
    CHECK (CGrade > DGrade),
    CHECK (DGrade >= 0)
);

CREATE TABLE CourseCategory (
    CatId MEDIUMINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    CatCourseId MEDIUMINT NOT NULL,
    CatName VARCHAR(60) NOT NULL,
    Percentage FLOAT NOT NULL,

    FOREIGN KEY (CatCourseId) REFERENCES Course(CId),
    CHECK (Percentage > 0 AND Percentage <= 100)
);
