package com.annis.tensioncable.My;

import java.io.File;

public class FileBean {
    private int file_type;
    private String file_name;
    private String file_time;
    private double file_size;
    private File file_path;

    public File getFile_path() {
        return file_path;
    }

    public void setFile_path(File file_path) {
        this.file_path = file_path;
    }

    public FileBean(int file_type, String file_name, String file_time, double file_size, File path) {
        this.file_type = file_type;
        this.file_name = file_name;
        this.file_time = file_time;
        this.file_size = file_size;
        this.file_path=path;
    }

    public int getFile_type() {
        return file_type;
    }

    public void setFile_type(int file_type) {
        this.file_type = file_type;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_time() {
        return file_time;
    }

    public void setFile_time(String file_time) {
        this.file_time = file_time;
    }

    public double getFile_size() {
        return file_size;
    }

    public void setFile_size(double file_size) {
        this.file_size = file_size;
    }
}
