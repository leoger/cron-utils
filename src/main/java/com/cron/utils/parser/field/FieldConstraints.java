package com.cron.utils.parser.field;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/*
 * Copyright 2014 jmrozanec
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Holds information on valid values for a field
 * and allows to perform mappings and validations.
 * Example of information for valid field values: int range,
 * valid special characters, valid nominal values.
 * Example for mappings: conversions from nominal values to integers
 * and integer-integer mappings if more than one integer
 * represents the same concept.
 */
public class FieldConstraints {
    private Map<String, Integer> stringMapping;
    private Map<Integer, Integer> intMapping;
    private Set<SpecialChar> specialChars;
    private int startRange;
    private int endRange;

    /**
     * Constructor
     * @param stringMapping - mapping of nominal values to integer equivalence
     * @param intMapping - mapping of integer values to another integer equivalence.
     *                   Always consider mapping higher integers to lower once.
     *                   Ex.: if 0 and 7 mean the same, map 7 to 0.
     * @param specialChars - allowed special chars
     * @param startRange - lowest possible value
     * @param endRange - highest possible value
     */
    public FieldConstraints(Map<String, Integer> stringMapping,
                            Map<Integer, Integer> intMapping,
                            Set<SpecialChar> specialChars,
                            int startRange, int endRange) {
        this.stringMapping = Collections.unmodifiableMap(stringMapping);
        this.intMapping = Collections.unmodifiableMap(intMapping);
        this.specialChars = Collections.unmodifiableSet(specialChars);
        this.startRange = startRange;
        this.endRange = endRange;
    }

    /**
     * Maps string expression to integer.
     * If no mapping is found, will try to parse String as Integer
     * @param exp - expression to be mapped
     * @return integer value for string expression
     */
    public int stringToInt(String exp) {
        if (stringMapping.containsKey(exp)) {
            return stringMapping.get(exp);
        } else {
            return Integer.parseInt(exp);
        }
    }

    /**
     * Maps integer values to another integer equivalence.
     * Always consider mapping higher integers to lower once.
     * Ex.: if 0 and 7 mean the same, map 7 to 0.
     * @param exp - integer to be mapped
     * @return Mapping integer. If no mapping int is found, will return exp
     */
    public int intToInt(Integer exp) {
        if (intMapping.containsKey(exp)) {
            return intMapping.get(exp);
        }
        return exp;
    }

    /**
     * Validate if given number is >= start range and <= end range
     * @param number - to be validated
     * @return - same number being validated if in range,
     * throws RuntimeException if number out of range
     */
    public int validateInRange(int number) {
        if (number >= startRange && number <= endRange) {
            return number;
        }
        throw new RuntimeException(String.format("Number %s out of range [%s,%s]", number, startRange, endRange));
    }

    /**
     * Validate if special char is allowed. If not, a RuntimeException will be raised.
     * @param specialChar - char to be validated
     */
    public void validateSpecialCharAllowed(SpecialChar specialChar){
        if(!specialChars.contains(specialChar)){
            throw new RuntimeException(String.format("Special char %s not supported!", specialChar));
        }
    }
}
