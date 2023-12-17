import { Component } from '@angular/core';
import { MenuItems } from 'src/app/models/menu-items';
import { DataTransferService } from 'src/app/services/data-transfer.service';

@Component({
  selector: 'app-item-manager',
  templateUrl: './item-manager.component.html',
  styleUrls: ['./item-manager.component.css']
})
export class ItemManagerComponent {

  private allItems: MenuItems[] = [];
  public newData: MenuItems = new MenuItems();
  public input_foodname: string = '';
  public input_allergens: string = '';

  public item: MenuItems = new MenuItems()

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
    console.log(this.input_allergens);
    
    this.newData = new MenuItems();
    this.newData.name = this.input_foodname;
    this.newData.allergens.push(this.input_allergens);
    
    this.allItems.push(this.newData);
    
    this.newMessage();
  }

  newMessage(){
    console.log("[LOG - new-message]");
    this.data.changeMessage(this.allItems);
    console.log(this.allItems);

  }
}
