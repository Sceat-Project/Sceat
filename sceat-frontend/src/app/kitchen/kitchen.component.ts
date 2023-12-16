import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import axios from 'axios';
import { AuthserviceService } from "../services/authservice.service";

enum Allergens {
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

    constructor(private authService: AuthserviceService) { }

    login() {
        this.authService.login('consumer-a@elte.hu', 'password').subscribe(loginResponse => {
            console.log('Login response:', loginResponse);
        });
    }

    getData() {
        this.authService.getData().subscribe(getResponse => {
            console.log('GET response:', getResponse);
        });
    }



}
