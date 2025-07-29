package com.linreelle.saphir.configuration;


import java.security.SecureRandom;
import java.util.Base64;
    public class HmacKeyGenerator {
        public static void main(String[] args) {
            SecureRandom secureRandom = new SecureRandom();
            byte[] key = new byte[64];  // 8 bytes = 64 bits
            secureRandom.nextBytes(key);

            // Optional: Encode to Base64 for easier storage or transmission
            String encodedKey = Base64.getEncoder().encodeToString(key);
            System.out.println("64-bit key (Base64): " + encodedKey);
        }
    }

