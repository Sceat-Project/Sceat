import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StatisticsViewComponent } from './statistics-view.component';

describe('StatisticsViewComponent', () => {
  let component: StatisticsViewComponent;
  let fixture: ComponentFixture<StatisticsViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StatisticsViewComponent]
    });
    fixture = TestBed.createComponent(StatisticsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
