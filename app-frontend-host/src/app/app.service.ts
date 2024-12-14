import { Injectable } from '@angular/core';
import { delay, Observable, of, BehaviorSubject, filter, map, from, concatMap } from 'rxjs';
import { DatabaseConfig } from './model/remote-entry-config';
import * as database from './data/database.json'
import { loadRemoteModule } from '@angular-architects/native-federation';
import { AppDataConfig } from './model/app-data-config.model';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  config: AppDataConfig | null = null;
  appConfigLoaded$ = new BehaviorSubject<boolean>(false);

  constructor() {
    this.fetchDbAppConfig();
  }

  fetchDbAppConfig(): void {

    const appDataConfig = AppDataConfig.instanceFromBrowserStorage();
    if(appDataConfig) {
      this.config = appDataConfig;
      this.appConfigLoaded$.next(true);
    } else {

      of<DatabaseConfig>(database)
        .pipe(delay(100))
        .subscribe(conf => {

          const appDataConfig = new AppDataConfig(conf.remoteEntries);
          appDataConfig.saveToBrowserStorage();
          this.config = appDataConfig;

          this.appConfigLoaded$.next(true);

        });

    };

  }

  loadRemoteEntryComponent$(name: string, component: string): Observable<any> {

    return this.loadAppDataConfig$()
      .pipe(
        map(config => config.remoteEntries),
        filter(entries => entries != null),
        map(entries => entries.find(entry => entry.name === name)),
        filter(entry => entry != null),
        concatMap(e => from(loadRemoteModule({
          remoteEntry: e.remoteEntry,
          exposedModule: component
        })))
      );

  }


  loadAppDataConfig$(): Observable<AppDataConfig> {

    return this.appConfigLoaded$
      .pipe(
        filter(loaded => loaded),
        concatMap(() => of(this.config)),
        filter(config => config != null),
      );

  }

}
