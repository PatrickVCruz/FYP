package com.cruz.fyp.virtualassistant.Azure.SpeechSynthesis;

public class Voice {

    final String lang;
    final String voiceName;
    final Gender gender;

    public enum Gender {
        Male, Female
    }

    Voice(String lang) {
        this.lang = lang;
        this.voiceName = "";
        this.gender = Gender.Female;
    }

    public Voice(String lang, String voiceName, Gender gender) {
        this.lang = lang;
        this.voiceName = voiceName;
        this.gender = gender;
    }
}
