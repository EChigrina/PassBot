package com.elizavetachigrina;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;

import java.util.Arrays;
import java.util.List;

public class PasswordGenerator {
    public static String generatePassword() {
        List<CharacterRule> rules = Arrays.asList(
                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),
                // at least one special character
                new CharacterRule(new CharacterData() {
                    public String getErrorCode() {
                        return "SAMPLE_ERROR_CODE";
                    }

                    public String getCharacters() {
                        return "!@#-_ ";
                    }
                }));

        org.passay.PasswordGenerator generator = new org.passay.PasswordGenerator();

        String password = generator.generatePassword(15, rules);

        return password;
    }
}
