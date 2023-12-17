import { Component } from '@angular/core';
import { MatInputModule } from "@angular/material/input";

@Component({
  selector: 'app-studentreader',
  templateUrl: './studentreader.component.html',
  styleUrls: ['./studentreader.component.css']
})
export class StudentreaderComponent {
    studentId: string = '';


    uploadToLocalStorage(): void {
        if (this.studentId) {
            if (localStorage.getItem('student') === null || localStorage.getItem('student') === '')
            {
                localStorage.setItem('student', this.studentId);
                console.log(localStorage.getItem('student'));
                this.studentId = '';
            }
            else
            {
                localStorage.setItem('student', localStorage.getItem('student') + ';' + this.studentId);
                console.log(localStorage.getItem('student'));
                this.studentId = '';
            }
        }
    }
}
