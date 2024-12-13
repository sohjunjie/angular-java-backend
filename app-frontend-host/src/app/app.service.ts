import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { delay, firstValueFrom, of, tap } from 'rxjs';
import { RemoteEntryConfig } from './model/remote-entry-config';
import * as frontEndConfigData from './data/frontend-config.json'

@Injectable({
  providedIn: 'root'
})
export class AppService {

  constructor(private httpClient: HttpClient) { }

  async getFederationJson() {

    return await firstValueFrom(

      of<RemoteEntryConfig[]>(frontEndConfigData.data)
        .pipe(delay(100))

    );

  }

}
