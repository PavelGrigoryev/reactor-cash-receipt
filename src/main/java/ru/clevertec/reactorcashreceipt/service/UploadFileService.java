package ru.clevertec.reactorcashreceipt.service;

import java.nio.file.Path;

public interface UploadFileService {

    Path uploadFile(String cashReceipt);

}
