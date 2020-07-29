package org.taipit.stend.controller;

import javax.crypto.*;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainStend {

    public static void main(String[] args) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-512");
            byte[] input = "%32342  DSAFJH ,IO'P[Secret string".getBytes();
            byte[] digest = digester.digest(input);
            System.out.println(DatatypeConverter.printHexBinary("%32342  DSAFJH ,IO'P[Secret string".getBytes()));
            String str = DatatypeConverter.printHexBinary(digest);
            System.out.println(str);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}