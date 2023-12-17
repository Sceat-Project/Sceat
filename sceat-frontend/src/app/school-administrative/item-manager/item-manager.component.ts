import { Component } from '@angular/core';
import { MenuItems } from 'src/app/models/menu-items';
import { DataTransferService } from 'src/app/services/data-transfer.service';
import {FormControl} from '@angular/forms';



@Component({
  selector: 'app-item-manager',
  templateUrl: './item-manager.component.html',
  styleUrls: ['./item-manager.component.css'],
})
export class ItemManagerComponent {

  private allItems: MenuItems[] = [];
  public newData: MenuItems = new MenuItems();
  public input_foodname: string = '';
  public input_ingredients: string = '';

  public item: MenuItems = new MenuItems()

  allergens = new FormControl('');
  allergensList: string[] = ['GLUTEN', 'PEANUTS', 'TREE_NUTS', 'CELERY', 'MUSTARD', 'EGGS', 'MILK', 'SESAME', 'FISH', 'CRUSTACEANS', 'MOLLUSCS', 'SOYA', 'SULPHITES', 'LUPIN', 'MEAT'];
  
  constructor(private data: DataTransferService) { 
  }

  ngOnInit(){
    this.newData = new MenuItems();
    this.data.currentMessage.subscribe(message => this.allItems = message);
    console.log("Add New Item page is working!");
  }

  onSubmit(){
    console.log("Submitted");
    console.log(this.input_foodname);
    console.log(this.input_ingredients.split(/\s*,\s*/));
    console.log(this.allergens.value);

    this.newData = new MenuItems();
    this.newData.name = this.input_foodname;
    for (var ingredient of this.input_ingredients.split(/\s*,\s*/)) {this.newData.foods.push(ingredient);}
    for (var allergen of this.allergens.value!){this.newData.allergens.push(allergen);}
    
    this.allItems.push(this.newData);
    
    this.newMessage();
  }

  newMessage(){
    console.log("[LOG - new-message]");
    this.data.changeMessage(this.allItems);
    console.log(this.allItems);

  }
}
