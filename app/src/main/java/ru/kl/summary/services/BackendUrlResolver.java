package ru.kl.summary.services;

import java.util.Scanner;

public class BackendUrlResolver {

    private static final String BACKEND_SITE_PROPERTIES_NAME = "backend.site";

    public BackendUrlResolver (){}

    public static String getBackendUrl (){
        String url = "";
        InternalResourcesHandler internalResourcesHandler = new InternalResourcesHandler();
        try {
            Scanner scanner = new Scanner(internalResourcesHandler.getAssetsPropFileContext());
            String tmpNextLIne = "";
            while (scanner.hasNextLine()) {
                tmpNextLIne = scanner.nextLine();
                if (tmpNextLIne.contains(BACKEND_SITE_PROPERTIES_NAME)) {
                    url = tmpNextLIne.split("=")[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }
}
