package org.hong.spring_ecommerce.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//Clase para subir imagenes asociadas a un producto y poder eliminarlas

@Service
public class UploadFileService {

    private String folder = "images//";

    //Tiene como parámetro MultipartFile que es la imagen
    public String saveImages(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            //Convertir a bytes para que se pueda enviar de un lugar a otro
            byte[] bytes = file.getBytes();
            //Variable de tipo Path y lo igualamos a la uri donde queremos que se almacene la imagen
            Path path = Paths.get(folder + file.getOriginalFilename());
            //Escribir los bytes en el archivo
            Files.write(path, bytes);
            return file.getOriginalFilename();
        }
        //Si la imagen está vacía... retornamos una imagen por defecto
        return "default.jpg";
    }

    //Eliminar la imagen cuando se eliminemos un producto pasándole la ruta + el nombre de la imagen
    public void deleteImage(String nombre) {
        String ruta = "images//";
        File file = new File(ruta + nombre);
        file.delete();

    }


}
