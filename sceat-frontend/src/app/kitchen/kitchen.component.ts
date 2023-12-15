import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import axios from 'axios';
import { AuthserviceService } from "../services/authservice.service";

enum Allergens {
    GLUTEN = "gluten-icon.png",
    PEANUTS = "peanuts-icon.png",
    TREE_NUTS = "tree_nuts-icon.png",
    CELERY = "celery-icon.png",
    MUSTARD = "mustard-icon.png",
    EGGS = "eggs-icon.png",
    MILK = "milk-icon.png",
    SESAME = "sesame-icon.png",
    FISH = "fish-icon.png",
    CRUSTACEANS = "crustacenas-icon.png",
    MOLLUSCS = "molluscs-icon.png",
    SOYA = "soya-icon.png",
    SULPHITES = "sulphites-icon.png",
    LUPIN = "lupin-icon.png",
    MEAT = "meat-icon.png"
}

@Component({
  selector: 'app-kitchen',
  templateUrl: './kitchen.component.html',
  styleUrls: ['./kitchen.component.css']
})
export class KitchenComponent implements OnInit {

    constructor(private authService: AuthserviceService) { }
    ngOnInit(): void {
        this.authService.login('consumer-a@elte.hu', 'password').subscribe(loginResponse => {
            console.log('Login response:', loginResponse.headers);

            // Make a GET request after logging in
            this.authService.getData().subscribe(getResponse => {
                console.log('GET response:', getResponse);
                // Handle the GET response as needed
            });
        });
    }

}
