import { RemoteEntryItem } from "./remote-entry-config";

export class AppDataConfig {

    static ExpireDuration : number = 1000 * 60 * 60 * 24 * 5;
    static BrowserStorageKey: string = "AppConfigWithExpiry";

    constructor(
        public remoteEntries: RemoteEntryItem[]
    ) {

    }

    public static instanceFromBrowserStorage(): AppDataConfig | null {

        const appConfigWithExpiryString = localStorage.getItem(AppDataConfig.BrowserStorageKey);

        if(!appConfigWithExpiryString) return null;

        const appConfigWithExpiry = JSON.parse(appConfigWithExpiryString);

        const expiry: number = appConfigWithExpiry.expiry;
        const appConfig: AppDataConfig = appConfigWithExpiry.appConfig;

        if(Date.now() > expiry) return null;

        return appConfig;

    }

    saveToBrowserStorage(): void {

        const expiry: number = Date.now() + AppDataConfig.ExpireDuration;
        const appConfigWithExpiry = {
            expiry: expiry,
            appConfig: this
        }
        const appConfigWithExpiryString = JSON.stringify(appConfigWithExpiry);
        localStorage.setItem(AppDataConfig.BrowserStorageKey, appConfigWithExpiryString);

    }

};
