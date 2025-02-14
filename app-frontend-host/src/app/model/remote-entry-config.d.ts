export type RemoteEntryItem = {

    name: string;
    remoteEntry: string;
    url: string;

};

export type DatabaseConfig = {

    remoteEntries: RemoteEntryItem[];

};
