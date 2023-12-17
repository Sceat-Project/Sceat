import { Component, Inject } from '@angular/core';
import { MenuItems } from '../models/menu-items';
import { Router } from '@angular/router';
import { DataTransferService } from '../services/data-transfer.service';
import { MAT_DIALOG_DATA, MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { NgFor } from '@angular/common';
import { MatCheckboxModule } from '@angular/material/checkbox';


export interface DialogData {
  id: number;
  name: string, 
  foods: string, 
  allergens: string, 
  occasion: string, 
  date: Date, 
  cost: number
}

@Component({
  selector: 'app-school-administrative',
  templateUrl: './school-administrative.component.html',
  styleUrls: ['./school-administrative.component.css']
})
export class SchoolAdministrativeComponent {
  
  public allItems: MenuItems[] = [];
  public item: MenuItems = new MenuItems();
  
  public messageFromOtherComponent: MenuItems[] = [];

  message: MenuItems = new MenuItems();

  public dialogdata: MenuItems = new MenuItems();

  constructor(
    //private menuService: MenuService, - GET miatt kellene
    private router: Router,
    private data: DataTransferService,
    public dialog: MatDialog){
  }

  async ngOnInit(): Promise<void> {
    //this.allItems = await this.menuService.getMenuItems();
    
    this.data.currentMessage.subscribe(message => this.messageFromOtherComponent = message);

    this.allItems = this.messageFromOtherComponent;
  }

  onButtonClick(){
    // Navigate to /item-manager page
    this.router.navigate(['/item-manager']);
  }

  // Creates a dialog box for the user to modify existing items
  editItem(oldItem:MenuItems){
    const dialogRef = this.dialog.open(ModificationDialog, {
      data: {id: oldItem.id, name: oldItem.name, foods: oldItem.foods, allergens: oldItem.allergens, occasion: oldItem.occasion, date: oldItem.date, cost: oldItem.cost},
    });

    // Getting back the results after a user modified an item
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.dialogdata = result;
      this.modifyItems();
    });
  }

  modifyItems(){

    var index = 0;
    while(this.allItems[index].id != this.dialogdata.id){
      index++;
    }
    var occasionOptions = ['FORENOON_FOOD', 'LUNCH', 'AFTERNOON_FOOD'];
    var allergenOptions = ['GLUTEN', 'PEANUTS', 'TREE_NUTS', 'CELERY', 'MUSTARD', 'EGGS', 'MILK', 'SESAME', 'FISH', 'CRUSTACEANS', 'MOLLUSCS', 'SOYA', 'SULPHITES', 'LUPIN', 'MEAT'];

    var modifiedItem = new MenuItems();
    modifiedItem.name = this.dialogdata.name;
    for (var ingredient of this.dialogdata.foods.toString().split(/\s*,\s*/)) {if (ingredient) {modifiedItem.foods.push(ingredient);}}
    for (var allergen of this.dialogdata.allergens.toString().split(/\s*,\s*/)) {if (allergenOptions.includes(allergen)) {modifiedItem.allergens.push(allergen);}}
    if(occasionOptions.includes(this.dialogdata.occasion)) {
      modifiedItem.occasion = this.dialogdata.occasion;
    } else {
      modifiedItem.occasion = '';
    }
    modifiedItem.cost = this.dialogdata.cost;

    console.log(this.dialogdata);
    console.log(modifiedItem);
    
    this.allItems[index] = modifiedItem;
    this.dialogdata = new MenuItems();
    this.newMessage();
  }

  newMessage(){
    this.data.changeMessage(this.allItems);
  }
  
}

// Component for dialog box
@Component({
  selector: 'modification-dialog',
  templateUrl: 'modification-dialog.html',
  standalone: true,
  imports: [MatDialogModule, MatFormFieldModule, MatInputModule, FormsModule, MatButtonModule, MatSelectModule, NgFor, MatCheckboxModule],
})
export class ModificationDialog {

  constructor(
    public dialogRef: MatDialogRef<ModificationDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
  ) {}

  // If the user chooses to Cancel and doesn't modifiy the item
  onNoClick(): void {
    this.dialogRef.close();
  }

}

