import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentreaderComponent } from './studentreader.component';

describe('StudentreaderComponent', () => {
  let component: StudentreaderComponent;
  let fixture: ComponentFixture<StudentreaderComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StudentreaderComponent]
    });
    fixture = TestBed.createComponent(StudentreaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
