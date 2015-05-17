package com.navid.trafalgar.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    private static final String MAP_EXTENSION = ".map";

    private FileUtils() {
    }

    /**
     * This method returns the list of files under a given directory, working
     * for both real folders and jar content.
     *
     * @param folder Folder
     * @param recursive Recursive?
     * @return List of strings with the path for the files (any).
     */
    public static List<String> findFilesInFolder(String folder, boolean recursive) {

        List<String> result = new ArrayList<>();

        URL url = FileUtils.class.getResource("/" + folder);

        if (url != null) {
            File directory;
            try {
                directory = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new InvalidParameterException(folder);
            }

            if (directory.exists()) {
                addAllFilesInDirectory(directory, folder, recursive, result);
            } else {
                try {
                    LOG.debug("Extracting files from JAR/ZIP file.");
                    URLConnection urlConnection = url.openConnection();

                    if (urlConnection instanceof JarURLConnection) {
                        JarURLConnection conn = (JarURLConnection) urlConnection;

                        JarFile jfile = conn.getJarFile();
                        Enumeration e = jfile.entries();
                        while (e.hasMoreElements()) {
                            ZipEntry entry = (ZipEntry) e.nextElement();
                            if (!entry.isDirectory() && entry.getName().contains(folder) && entry.getName().endsWith(MAP_EXTENSION)) {
                                LOG.debug("Adding entry {} to map finding.", entry.getName());
                                result.add(entry.getName());
                            }
                        }
                    }
                } catch (Exception e) {
                    LOG.error("Error in findFilesInFolder", e);
                }
            }
        }
        return result;
    }

    private static void addAllFilesInDirectory(File directory, String folder, boolean recursive, List<String> result) {
        LOG.debug("Extracting files from real directory.");
        // Get the list of the files contained in the package
        File[] files = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(MAP_EXTENSION);
            }
        });
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                // we are only interested in .class files
                if (files[i].isDirectory()) {
                    if (recursive) {
                        addAllFilesInDirectory(files[i],
                                folder + files[i].getName() + ".", true, result);
                    }
                } else {
                    result.add(folder + files[i].getName());
                }
            }
        }
    }
}
