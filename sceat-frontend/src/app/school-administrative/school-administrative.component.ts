import { Component } from '@angular/core';
import { MenuItems } from '../models/menu-items';
import { Router } from '@angular/router';
import { DataTransferService } from '../services/data-transfer.service';

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

  constructor(
    //private menuService: MenuService, - GET miatt kellene
    private router: Router,
    private data: DataTransferService){

  }

  async ngOnInit(): Promise<void> {
    //this.allItems = await this.menuService.getMenuItems();
    
    this.data.currentMessage.subscribe(message => this.messageFromOtherComponent = message);

    this.allItems = this.messageFromOtherComponent;
  }

  onButtonClick(){
    // Navigate to /products page
    this.router.navigate(['/item-manager']);
  }

  newMessage(){
    this.data.changeMessage(this.allItems);
  }
  
}
