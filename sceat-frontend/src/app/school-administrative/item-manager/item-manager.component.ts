import { Component } from '@angular/core';
import { MenuItems } from 'src/app/models/menu-items';
import { DataTransferService } from 'src/app/services/data-transfer.service';
import {FormControl} from '@angular/forms';

interface Occasions {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-item-manager',
  templateUrl: './item-manager.component.html',
  styleUrls: ['./item-manager.component.css'],
})
export class ItemManagerComponent {

  // Array of all the food items in the database
  private allItems: MenuItems[] = [];

  // Variables for user input
  public newData: MenuItems = new MenuItems();
  public input_foodname: string = '';
  public input_ingredients: string = '';
  public input_occasion: string = '';
  public input_cost: number = 0;
  public input_date: Date = new Date();
  public item: MenuItems = new MenuItems()

  occasions: Occasions[] = [
    {value: 'FORENOON_FOOD', viewValue: 'Tízórai'},
    {value: 'LUNCH', viewValue: 'Ebéd'},
    {value: 'AFTERNOON_FOOD', viewValue: 'Uzsonna'},
  ];

  allergens = new FormControl('');
  allergensList: string[] = ['GLUTEN', 'PEANUTS', 'TREE_NUTS', 'CELERY', 'MUSTARD', 'EGGS', 'MILK', 'SESAME', 'FISH', 'CRUSTACEANS', 'MOLLUSCS', 'SOYA', 'SULPHITES', 'LUPIN', 'MEAT'];
  
  public globalId: number = 0;

  constructor(private data: DataTransferService) { 
  }

  ngOnInit(){
    this.newData = new MenuItems();
    this.data.currentMessage.subscribe(message => this.allItems = message);
    console.log("Add New Item page is working!");
    this.globalId = this.allItems[this.allItems.length-1].id;
  }

  onSubmit(){
    
    // Debugging
    console.log("Submitted");
    console.log("Napszak: "+this.input_occasion+" Dárum: "+this.input_date);
    console.log("Étel neve: "+this.input_foodname+" Összetevői: "+this.input_ingredients.split(/\s*,\s*/)+" Allergén adatok: "+this.allergens.value+" Ár: "+this.input_cost);

    // Adding new user inputted item into the list
    this.globalId++;
    this.newData = new MenuItems();
    this.newData.id = this.globalId;
    this.newData.occasion = this.input_occasion;
    this.newData.date = this.input_date;
    this.newData.name = this.input_foodname;
    for (var ingredient of this.input_ingredients.split(/\s*,\s*/)) {if (ingredient) {this.newData.foods.push(ingredient);}}
    for (var allergen of this.allergens.value!){this.newData.allergens.push(allergen);}
    this.newData.cost = this.input_cost;
    
    this.allItems.push(this.newData);
    
    this.newMessage();
  }

  newMessage(){
    console.log("[LOG - new-message]");
    this.data.changeMessage(this.allItems);
    console.log(this.allItems);
  }
}
