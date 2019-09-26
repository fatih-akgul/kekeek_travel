package com.kekeek.travel.util;

import com.kekeek.travel.model.Content;
import com.kekeek.travel.model.PageHierarchy;
import com.kekeek.travel.model.SitePage;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class ContentLoader {

    private final static String TOP_DIR_NAME = "src/main/resources/content";
    private final static String API_URL = System.getenv("API_SERVER") + "/pages";

    private static String username = System.getenv("API_USERNAME");
    private static String password = System.getenv("API_PASSWORD");

    private static RestTemplate restTemplate = new RestTemplateBuilder().basicAuthentication(username, password).build();

    public static void main(String[] args) {
        File topDir = new File(TOP_DIR_NAME);
        File[] subDirs = topDir.listFiles(File::isDirectory);

        try {
            // Process each top level directory with null parent
            Stream.of(Objects.requireNonNull(subDirs)).forEach(subDir -> processFolder(subDir, null));

            // Process non-primary parent-child relationships
            Stream.of(Objects.requireNonNull(subDirs)).forEach(ContentLoader::processHierarchies);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processFolder(File folder, File parentFolder) {
        File[] folderContents = folder.listFiles();

        // Return if empty folder
        if (folderContents == null) {
            System.out.println("There are no files or subdirectories under " + folder.getAbsolutePath());
            return;
        }

        // If there is a .json file:
        // - Create a page with current directory name if one doesn't exist
        // - Create page based on .json file
        // - Create content elements based on the .html files in the folder
        String pageIdentifier = folder.getName();
        File[] jsonFiles = folder.listFiles(f -> f.getName().equals(pageIdentifier + ".json"));
        Stream.of(Objects.requireNonNull(jsonFiles)).forEach(jsonFile -> {
            SitePage page = null;
            try {
                page = findAndUpdatePage(jsonFile, parentFolder);
            } catch (HttpClientErrorException.NotFound e) {
                page = addPage(jsonFile, parentFolder);
            } catch (Exception e) {
                System.out.println("Exception of type " + e.getClass() + " while getting page " + pageIdentifier);
            }

            if (page != null) {
                processPageHierarchy(page, parentFolder);
                processPageContents(page, folder);
            }
        });

        // Process each subdirectory that has a .json file in it
        Stream.of(folderContents)
                .filter(File::isDirectory)
                .filter(d -> Files.exists(Paths.get(d.getAbsolutePath() + "/" + d.getName() + ".json"))) // contains .../pageName/pageName.json
                .forEach(subDir -> processFolder(subDir, folder));
    }

    private static void processHierarchies(File folder) {
        File[] folderContents = folder.listFiles();

        // Return if empty folder
        if (folderContents == null) {
            System.out.println("There are no files or subdirectories under " + folder.getAbsolutePath());
            return;
        }

        // Process each subdirectory in it
        Stream.of(folderContents)
                .filter(File::isDirectory)
                .forEach(ContentLoader::processHierarchies);

        // Process non-primary child relationships (childArticle-childIdentifier.json)
        File[] hierarchyFiles = folder.listFiles(f -> f.getName().startsWith("childArticle-") && f.getName().endsWith(".json"));
        Stream.of(Objects.requireNonNull(hierarchyFiles)).forEach(ContentLoader::processPageHierarchyJson);
    }

    private static void processPageHierarchyJson(File jsonFile) {
        try {
            String fileName = jsonFile.getName();

            String jsonStr = Files.readString(jsonFile.toPath());
            PageHierarchy pageHierarchy = PageHierarchy.fromJson(jsonStr, PageHierarchy.class);
            pageHierarchy.setPrimary(Boolean.FALSE);
            pageHierarchy.setParentIdentifier(jsonFile.getParentFile().getName());
            pageHierarchy.setChildIdentifier(fileName.substring(fileName.indexOf("-") + 1, fileName.indexOf(".json")));

            postPageHierarchy(pageHierarchy);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    private static HttpEntity<String> getPageRequest(File jsonFile, File parentFolder) throws IOException {
        String jsonStr = Files.readString(jsonFile.toPath());

        SitePage page = SitePage.fromJson(jsonStr, SitePage.class);
        page.setIdentifier(jsonFile.getParentFile().getName());
        if (parentFolder != null) {
            page.setParentPageIdentifier(parentFolder.getName());
        }

        jsonStr = page.toJson();
        return new HttpEntity<>(jsonStr, getHeaders());
    }

    private static SitePage findAndUpdatePage(File jsonFile, File parentFolder) throws HttpClientErrorException.NotFound {
        String pageIdentifier = jsonFile.getParentFile().getName();
        String url = API_URL + "/" + pageIdentifier;
        SitePage page = restTemplate.getForObject(url, SitePage.class);
        try {
            restTemplate.put(url, getPageRequest(jsonFile, parentFolder));
        } catch (IOException ex) {
            System.out.println("Exception of type " + ex.getClass()
                    + " while updating page from file " + jsonFile.getAbsolutePath()
                    + ": " + ex.getMessage());
        }

        return page;
    }

    private static SitePage addPage(File jsonFile, File parentFolder) {
        try {
            return restTemplate.postForObject(API_URL, getPageRequest(jsonFile, parentFolder), SitePage.class);
        } catch (IOException ex) {
            System.out.println("IOException of type " + ex.getClass()
                    + " while adding page from file " + jsonFile.getAbsolutePath() + ": "
                    + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Exception of type " + ex.getClass()
                    + " while adding page from file " + jsonFile.getAbsolutePath() + ": "
                    + ex.getMessage());

            throw ex;
        }

        return null;
    }

    private static void processPageHierarchy(SitePage page, File parentFolder) {
        if (parentFolder != null) {
            try {
                PageHierarchy pageHierarchy = new PageHierarchy();
                pageHierarchy.setChildIdentifier(page.getIdentifier());
                pageHierarchy.setParentIdentifier(parentFolder.getName());
                pageHierarchy.setSequence(page.getSequence());
                pageHierarchy.setPrimary(Boolean.TRUE);

                postPageHierarchy(pageHierarchy);
            } catch (IOException ex) {
                System.out.println("Exception of type " + ex.getClass()
                        + " while adding page hierarchy " + parentFolder.getName() + " -> " + page.getIdentifier() + ": "
                        + ex.getMessage());
            }
        }
    }

    private static void postPageHierarchy(PageHierarchy pageHierarchy) throws IOException {
        String jsonStr = pageHierarchy.toJson();
        String url = API_URL + "/hierarchy";
        restTemplate.postForObject(url, new HttpEntity<>(jsonStr, getHeaders()), PageHierarchy.class);
    }

    private static void processPageContents(SitePage page, File folder) {
        String pageName = page.getIdentifier();

        File[] htmlFiles = folder.listFiles(f -> f.isFile() && f.getName().endsWith(".html"));
        if (htmlFiles != null) {
            Stream.of(htmlFiles).forEach(htmlFile -> {
                String contentIdentifier = getFileNameWithoutExtension(htmlFile);
                System.out.println(">>>>> " + pageName + "/" + contentIdentifier);

                String baseUrl = API_URL + "/" + pageName + "/contents";
                try {
                    findAndUpdateContent(baseUrl, htmlFile);
                } catch (HttpClientErrorException.NotFound e) {
                    addContent(baseUrl, htmlFile);
                } catch (Exception e) {
                    System.out.println("Exception of type " + e.getClass()
                            + " while findAndUpdate of content " + baseUrl + "/" + contentIdentifier);
                }
            });
        }
    }

    private static void findAndUpdateContent(String baseUrl, File htmlFile) throws HttpClientErrorException.NotFound {
        String url = baseUrl + "/" + getFileNameWithoutExtension(htmlFile);
        restTemplate.getForObject(url, Content.class);
        try {
            restTemplate.put(url, getContentRequest(htmlFile));
        } catch (IOException e) {
            System.out.println("Exception of type " + e.getClass()
                    + " while updating content " + url
                    + " from file " + htmlFile.getAbsolutePath()
                    + ": " + e.getMessage());
        }
    }

    private static void addContent(String baseUrl, File htmlFile) {
        try {
            restTemplate.postForObject(baseUrl, getContentRequest(htmlFile), Content.class);
        } catch (Exception e) {
            System.out.println("Exception of type " + e.getClass()
                    + " while adding content " + baseUrl + "/" + getFileNameWithoutExtension(htmlFile)
                    + " from file " + htmlFile.getAbsolutePath() + ": "
                    + e.getMessage());
        }
    }

    private static HttpEntity<String> getContentRequest(File htmlFile) throws IOException {
        Content content = new Content();
        String contentIdentifier = getFileNameWithoutExtension(htmlFile);
        File jsonFile = new File(htmlFile.getParent() + "/" + contentIdentifier + ".content.json");

        if (jsonFile.exists()) {
            String jsonStr = Files.readString(jsonFile.toPath());
            content = Content.fromJson(jsonStr, Content.class);
        }
        content.setIdentifier(contentIdentifier);
        content.setContentText(Files.readString(htmlFile.toPath()));

        return new HttpEntity<>(content.toJson(), getHeaders());
    }

    private static String getFileNameWithoutExtension(File file) {
        return file.getName().substring(0, file.getName().lastIndexOf("."));
    }
}
