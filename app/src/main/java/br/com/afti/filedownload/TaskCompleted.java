package br.com.afti.filedownload;

public interface TaskCompleted {
    // Define data you like to return from AysncTask
    public void onTaskComplete(String result);
}