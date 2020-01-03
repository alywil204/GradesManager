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
    CatType ENUM('Single Grade', 'Multiple Grade'),

    FOREIGN KEY (CatCourseId) REFERENCES Course(CId),
    CHECK (Percentage > 0)
);

CREATE TABLE CategoryPoints (
    CatId MEDIUMINT UNIQUE NOT NULL,
    MaxPoints SMALLINT,
    Points SMALLINT,

    FOREIGN KEY (CatId) REFERENCES CourseCategory(CatId),
    CHECK (MaxPoints > 0 AND Points > 0)
);

CREATE TABLE CategoryAssignments (
    CatId MEDIUMINT NOT NULL,
    AName VARCHAR(60) NOT NULL,
    MaxPoints SMALLINT,
    Points SMALLINT,
    Weight FLOAT,

    FOREIGN KEY (CatId) REFERENCES CourseCategory(CatId),
    CHECK (MaxPoints > 0 AND Points > 0 AND Weight > 0)
);
