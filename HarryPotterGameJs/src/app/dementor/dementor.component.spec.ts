import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DementorComponent } from './dementor.component';

describe('DementorComponent', () => {
  let component: DementorComponent;
  let fixture: ComponentFixture<DementorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DementorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DementorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
