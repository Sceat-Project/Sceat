import { KeyValuePipe } from '@angular/common';
import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
//import { Observable, tap } from 'rxjs';
//import { AuthserviceService } from '../services/authservice.service';
//import { LoginFormComponent } from '../login-form/login-form.component';

//import { MatDatepickerModule } from '@angular/material/datepicker';
//import { MatNativeDateModule } from '@angular/material/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Allergens } from '../kitchen/kitchen.component';

type Menu = {
    id: number;
    organization: any;
    name: string;
    date: string;
    occassion: string;
    cost: number;
    foods: string[];
    allergens: string[];
};

type MenuCount = {
    menu: Menu;
    count: number;
};

@Component({
    selector: 'app-statistics-view',
    templateUrl: './statistics-view.component.html',
    styleUrls: ['./statistics-view.component.css'],
})
export class StatisticsViewComponent {
    range = {
        start: '2023-12-11',
        end: '2023-12-25',
    };
    menusPurchased: MenuCount[] = [];
    /* customerAllergies: Allergens[] = []; */

    constructor(private http: HttpClient) {}

    Object = Object;

    ngOnInit() {
        this.refreshData();
    }

    refreshData() {
        this.http
            .get<MenuCount[]>(
                `http://localhost:8080/api/organization/id/1/menuPurchaseCounts?startDate=${this.range.start}&endDate=${this.range.end}`
            )
            .subscribe((data) => (this.menusPurchased = data));
    }

    /* range = new FormGroup({
        start: new FormControl<Date | null>(null),
        end: new FormControl<Date | null>(null),
    }); */

    groupby(array: any, key: any) {
        return array.reduce(function (rv: any, x: any) {
            (rv[x[key]] = rv[x[key]] || []).push(x);
            return rv;
        }, {});
    }
    //console.log(groupBy(['one', 'two', 'three'], 'length'));
    // => {"3": ["one", "two"], "5": ["three"]}

    menuMap(menus: MenuCount[]): any {
        return menus.map((m) => m.menu);
    }

    menuFilterByDate(menus: MenuCount[], date: string): MenuCount[] {
        return menus.filter((m) => m.menu.date == date && m.count > 0);
    }

    //dates = Object.keys(this.groupby(this.menuMap(this.menusPurchased),'date'))

    allergens = [
        'GLUTEN',
        'EGGS',
        'MILK',
        'PEANUTS',
        'TREE_NUTS',
        'CELERY',
        'MUSTARD',
        'SESAME',
        'FISH',
        'CRUSTACEANS',
        'MOLLUSCS',
        'SOYA',
        'SULPHITES',
        'LUPIN',
        'MEAT',
    ];

    getTooltip(key: string) {
        let returnValue;

        switch (key) {
            case 'GLUTEN':
                returnValue = 'Glutén allergia';
                break;
            case 'PEANUTS':
                returnValue = 'Mogyoróallergia';
                break;
            case 'TREE_NUTS':
                returnValue = 'Diófélék allergia';
                break;
            case 'CELERY':
                returnValue = 'Zeller allergia';
                break;
            case 'MUSTARD':
                returnValue = 'Mustár allergia';
                break;
            case 'EGGS':
                returnValue = 'Tojás allergia';
                break;
            case 'MILK':
                returnValue = 'Tej allergia';
                break;
            case 'SESAME':
                returnValue = 'Szezámmag allergia';
                break;
            case 'FISH':
                returnValue = 'Hal allergia/Vegetárináus';
                break;
            case 'CRUSTACEANS':
                returnValue = 'Rákféle allergia/Vegetáriánus';
                break;
            case 'MOLLUSCS':
                returnValue = 'Tengergyümölcsei allergia';
                break;
            case 'SOYA':
                returnValue = 'Szója allergia';
                break;
            case 'SULPHITES':
                returnValue = 'Szulfát allergia';
                break;
            case 'LUPIN':
                returnValue = 'Lupin allergia';
                break;
            case 'MEAT':
                returnValue = 'Hús allergia/Vegetáriánus';
                break;
        }

        return returnValue;
    }

    getRandomInt(max: number) {
        return Math.floor(Math.random() * max);
    }

}
