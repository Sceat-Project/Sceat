import { Component } from '@angular/core';
import { NgOptimizedImage} from "@angular/common";
import {AuthserviceService} from "./services/authservice.service";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'sceat-frontend';
  variable = 1+5;
  currentStudent: any;
  currStudentAllergies: any[] = [];

    constructor(private authService: AuthserviceService) { }

    addToList(newElement: any) {
        this.currStudentAllergies.push(newElement);
    }

    login() {
        this.authService.login('consumer-a@elte.hu', 'password').subscribe(loginResponse => {
            console.log('Login response:', loginResponse);
        });
    }

    getData() {
        this.authService.getData().subscribe(getResponse => {
            console.log('GET response:', getResponse);

            this.currentStudent = getResponse;
            this.currStudentAllergies = getResponse["consumerProfile"]["allergies"];
            this.addToList('GLUTEN'); this.addToList('MILK');
            console.log(this.currStudentAllergies);
        });
    }

    protected readonly JSON = JSON;
}
