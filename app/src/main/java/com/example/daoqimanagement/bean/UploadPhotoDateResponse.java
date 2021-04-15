package com.example.daoqimanagement.bean;

public class UploadPhotoDateResponse {
    @Override
    public String toString() {
        return "UploadPhotoDateResponse{" +
                "pictureId=" + pictureId +
                '}';
    }

    /**
     * pictureId : 2
     */


    private int pictureId;

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }
}
