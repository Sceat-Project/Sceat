import { Component } from '@angular/core';
import { AuthserviceService } from "../services/authservice.service";

export enum Allergens {
    GLUTEN = "gluten-free.png",
    PEANUTS = "nut-free.png",
    TREE_NUTS = "tree-nuts-free.png",
    CELERY = "celery-free.png",
    MUSTARD = "mustard-free.png",
    EGGS = "eggs-free.png",
    MILK = "milk-free.png",
    SESAME = "sesame-free.png",
    FISH = "fish-free.png",
    CRUSTACEANS = "shrimp-free.png",
    MOLLUSCS = "shellfish-free.png",
    SOYA = "soy-free.png",
    SULPHITES = "sulphite-free.png",
    LUPIN = "lupin-free.png",
    MEAT = "meat-free.png"
}

@Component({
  selector: 'app-kitchen',
  templateUrl: './kitchen.component.html',
  styleUrls: ['./kitchen.component.css']
})
export class KitchenComponent {


    enumKeys = ['GLUTEN', 'EGGS', 'MILK', 'PEANUTS', 'TREE_NUTS', 'CELERY', 'MUSTARD', 'SESAME', 'FISH', 'CRUSTACEANS', 'MOLLUSCS', 'SOYA', 'SULPHITES', 'LUPIN', 'MEAT'];

    constructor(private authService: AuthserviceService) { }

    login() {
        this.authService.login('consumer-a@elte.hu', 'password').subscribe(loginResponse => {
            console.log('Login response:', loginResponse);
        });
    }

    getData(): any {
        this.authService.getData().subscribe(getResponse => {
            console.log('GET response:', getResponse);
            return getResponse;
        });
    }

    getImage(key: string) {

        let returnValue;

        switch (key) {
            case ('GLUTEN'):
                returnValue = "gluten-free.png";
                break;
            case('PEANUTS'):
                returnValue = "nut-free.png";
                break;
            case('TREE_NUTS'):
                returnValue = "tree-nuts-free.png";
                break;
            case('CELERY'):
                returnValue = "celery-free.png";
                break;
            case('MUSTARD'):
                returnValue = "mustard-free.png";
                break;
            case('EGGS'):
                returnValue = "egg-free.png";
                break;
            case('MILK'):
                returnValue = "milk-free.png";
                break;
            case('SESAME'):
                returnValue = "sesame-free.png";
                break;
            case('FISH'):
                returnValue = "fish-free.png";
                break;
            case('CRUSTACEANS'):
                returnValue = "shrimp-free.png";
                break;
            case('MOLLUSCS'):
                returnValue = "shellfish-free.png";
                break;
            case('SOYA'):
                returnValue = "soy-free.png";
                break;
            case('SULPHITES'):
                returnValue = "sulphite-free.png";
                break;
            case('LUPIN'):
                returnValue = "lupin-free.png";
                break;
            case('MEAT'):
                returnValue = "meat-free.png";
                break;
        }

        return returnValue;
    }

    getTooltip(key: string) {

        let returnValue;

        switch (key) {
            case ('GLUTEN'):
                returnValue = "Glutén allergia";
                break;
            case('PEANUTS'):
                returnValue = "Mogyoróallergia";
                break;
            case('TREE_NUTS'):
                returnValue = "Diófélék allergia";
                break;
            case('CELERY'):
                returnValue = "Zeller allergia";
                break;
            case('MUSTARD'):
                returnValue = "Mustár allergia";
                break;
            case('EGGS'):
                returnValue = "Tojás allergia";
                break;
            case('MILK'):
                returnValue = "Tej allergia";
                break;
            case('SESAME'):
                returnValue = "Szezámmag allergia";
                break;
            case('FISH'):
                returnValue = "Hal allergia/Vegetárináus";
                break;
            case('CRUSTACEANS'):
                returnValue = "Rákféle allergia/Vegetáriánus";
                break;
            case('MOLLUSCS'):
                returnValue = "Tengergyümölcsei allergia";
                break;
            case('SOYA'):
                returnValue = "Szója allergia";
                break;
            case('SULPHITES'):
                returnValue = "Szulfát allergia";
                break;
            case('LUPIN'):
                returnValue = "Lupin allergia";
                break;
            case('MEAT'):
                returnValue = "Hús allergia/Vegetáriánus";
                break;
        }

        return returnValue;
    }



    protected readonly Allergens = Allergens;
}
