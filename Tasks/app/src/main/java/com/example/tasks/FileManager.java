package com.example.tasks;

import android.content.Context;

import com.example.tasks.dataStructure.User;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileManager {
    private static final String FILENAME = "TasksData.txt";

    public static void writeUser(Context context, User user) {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(user);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User readUser(Context context) {
        User user = new User();
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            if (obj instanceof User) {
                user = (User) obj;
            }
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
