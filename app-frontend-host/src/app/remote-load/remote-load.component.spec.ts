import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemoteLoadComponent } from './remote-load.component';

describe('RemoteLoadComponent', () => {
  let component: RemoteLoadComponent;
  let fixture: ComponentFixture<RemoteLoadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RemoteLoadComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RemoteLoadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
