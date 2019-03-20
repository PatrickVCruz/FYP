package com.cruz.fyp.virtualassistant.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ULDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UL_DATA";

    private static final int DATABASE_VERSION = 1;

    ULDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        String DATABASE_MODULES = "CREATE TABLE IF NOT EXISTS Modules(\n" +
                "   Module_Code    VARCHAR(7) NOT NULL PRIMARY KEY\n" +
                "  ,Module_Title2  VARCHAR(121) NOT NULL\n" +
                ");";

        database.execSQL(DATABASE_MODULES);

        String DATABASE_ROOMS = "CREATE TABLE IF NOT EXISTS Rooms (\n" +
                "    `Room` VARCHAR(8) NOT NULL PRIMARY KEY,\n" +
                "    `Building` VARCHAR(23) NOT NULL,\n" +
                "    `Size` INT\n" +
                ");";
        database.execSQL(DATABASE_ROOMS);

        database.execSQL("INSERT INTO Rooms VALUES\n" +
                "    ('AD2010','Analog Devices Building',205),\n" +
                "    ('CSG01','Computer Science',210),\n" +
                "    ('CSG25','Computer Science',38),\n" +
                "    ('ERB001','Engineering Research',106),\n" +
                "    ('ERB006','Engineering Research',30),\n" +
                "    ('ERB007','Engineering Research',30),\n" +
                "    ('ERB008','Engineering Research',35),\n" +
                "    ('ERO008','Engineering Research',25),\n" +
                "    ('FB028','Foundation',148),\n" +
                "    ('FG042','Foundation',233),\n" +
                "    ('FG061','Foundation',750),\n" +
                "    ('HSG008A','Health Sciences ',35),\n" +
                "    ('HSG021','Health Sciences ',30),\n" +
                "    ('HSG022','Health Sciences ',35),\n" +
                "    ('HSG023','Health Sciences ',30),\n" +
                "    ('HSG024','Health Sciences ',30),\n" +
                "    ('HSG025','Health Sciences ',35),\n" +
                "    ('HSG030','Health Sciences ',50),\n" +
                "    ('HSG031','Health Sciences ',40),\n" +
                "    ('HSG037','Health Sciences ',146),\n" +
                "    ('KB118','Kemmy Business',30),\n" +
                "    ('KB119','Kemmy Business',30),\n" +
                "    ('KBG10','Kemmy Business',48),\n" +
                "    ('KBG11','Kemmy Business',48),\n" +
                "    ('KBG12','Kemmy Business',346),\n" +
                "    ('KBG13','Kemmy Business',50),\n" +
                "    ('KBG14','Kemmy Business',50),\n" +
                "    ('KBG15','Kemmy Business',50),\n" +
                "    ('LCB002','Languages',20),\n" +
                "    ('LCB003','Languages',20),\n" +
                "    ('LCB009','Languages',25),\n" +
                "    ('LCB009A','Languages',25),\n" +
                "    ('LCB010','Languages',28),\n" +
                "    ('LCB015','Languages',20),\n" +
                "    ('LCO002','Languages',22),\n" +
                "    ('LCO003','Languages',22),\n" +
                "    ('LCO017','Languages',31),\n" +
                "    ('LG011','Lonsdale',60),\n" +
                "    ('A1050','Main',20),\n" +
                "    ('A1051','Main',20),\n" +
                "    ('A1052','Main',35),\n" +
                "    ('A1053','Main',50),\n" +
                "    ('A1054','Main',35),\n" +
                "    ('A1055','Main',50),\n" +
                "    ('B1005','Main',25),\n" +
                "    ('B1005A','Main',25),\n" +
                "    ('B1023','Main',232),\n" +
                "    ('BM015','Main',60),\n" +
                "    ('C0078','Main',30),\n" +
                "    ('C0079','Main',30),\n" +
                "    ('C1058','Main',50),\n" +
                "    ('C1059','Main',60),\n" +
                "    ('C1060','Main',60),\n" +
                "    ('C1061','Main',60),\n" +
                "    ('C1062','Main',40),\n" +
                "    ('C1063','Main',110),\n" +
                "    ('CG053','Main',30),\n" +
                "    ('CG054','Main',30),\n" +
                "    ('CG055','Main',30),\n" +
                "    ('CG057','Main',25),\n" +
                "    ('CG058','Main',30),\n" +
                "    ('CG059','Main',30),\n" +
                "    ('D1049','Main',30),\n" +
                "    ('D1050','Main',110),\n" +
                "    ('DG016','Main',325),\n" +
                "    ('EG002','Main',25),\n" +
                "    ('EG003','Main',25),\n" +
                "    ('EG004','Main',25),\n" +
                "    ('EG005','Main',25),\n" +
                "    ('EG006','Main',25),\n" +
                "    ('EM009','Main',40),\n" +
                "    ('EM010','Main',40),\n" +
                "    ('GEMS0016','Medical School',144),\n" +
                "    ('GEMS0028','Medical School',60),\n" +
                "    ('P1003','Old PE Building',18),\n" +
                "    ('P1004','Old PE Building',30),\n" +
                "    ('P1005','Old PE Building',30),\n" +
                "    ('P1006','Old PE Building',30),\n" +
                "    ('P1031','Old PE Building',36),\n" +
                "    ('P1033','Old PE Building',150),\n" +
                "    ('SR2027','Schrodinger',30),\n" +
                "    ('SR2028','Schrodinger',28),\n" +
                "    ('SR2029','Schrodinger',28),\n" +
                "    ('SR2030','Schrodinger',28),\n" +
                "    ('SR3006','Schrodinger',104),\n" +
                "    ('SR3007','Schrodinger',104),\n" +
                "    ('SR3008','Schrodinger',104),\n" +
                "    ('S114','Schuman',60),\n" +
                "    ('S115','Schuman',60),\n" +
                "    ('S116','Schuman',55),\n" +
                "    ('S205','Schuman',107),\n" +
                "    ('S206','Schuman',110),\n" +
                "    ('SG15','Schuman',60),\n" +
                "    ('SG16','Schuman',60),\n" +
                "    ('SG17','Schuman',60),\n" +
                "    ('SG18','Schuman',45),\n" +
                "    ('SG19','Schuman',35),\n" +
                "    ('SG20','Schuman',55),\n" +
                "    ('SG21','Schuman',30),\n" +
                "    ('SG21A','Schuman',20),\n" +
                "    ('S117','Schuman ',42);\n");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Database_Helper",
                "Upgrading database from " + oldVersion +" to "+newVersion);
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_NAME );
        onCreate(db);
    }

}
