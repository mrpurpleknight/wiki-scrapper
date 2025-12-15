    package com.lastminute.recruitment.domain.client;

    import com.lastminute.recruitment.domain.error.WikiPageNotFound;
    import com.lastminute.recruitment.domain.model.WikiPage;

    import java.net.URI;

    public interface WikiReaderClient {
        WikiPage read(String link);

        private String extractPageName(String link) {
            String path = URI.create(link).getPath();
            if (path == null || path.isBlank()) {
                throw new WikiPageNotFound(link);
            }
            return path.replaceFirst("^/", "");
        }

        default String resolveResourcePath(String link, String basePath, String extension) {
            String pageName = extractPageName(link);
            return basePath + "/" + pageName + "." + extension;
        }
    }
