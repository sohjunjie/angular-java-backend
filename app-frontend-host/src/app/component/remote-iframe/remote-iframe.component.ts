import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { AppService } from '../../app.service';

@Component({
  selector: 'app-remote-iframe',
  standalone: true,
  imports: [],
  templateUrl: './remote-iframe.component.html',
  styleUrl: './remote-iframe.component.css'
})
export class RemoteIframeComponent implements OnInit {

  iframeUrl: SafeResourceUrl | undefined = undefined;

  constructor(
    private sanitizer: DomSanitizer,
    private appService: AppService
  ) {

  }

  ngOnInit(): void {

    this.appService.loadRemoteEntryUrl$('app-frontend-remote')
      .subscribe(url => {
        console.log(url);
        this.iframeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
      });

  }

}
