package com.mykosan.utils;

import org.mindrot.jbcrypt.BCrypt;
import java.util.Scanner;

public class HashGenerator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan password yang ingin di-hash: ");
        String plainPassword = scanner.nextLine();

        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
        System.out.println("🔐 Hasil Hash: " + hashed);
    }
}
