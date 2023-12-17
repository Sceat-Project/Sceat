import { Injectable } from '@angular/core';
import { MenuItems } from '../models/menu-items';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataTransferService {
  
  menuItem:MenuItems[] = [];
  private messageSource = new BehaviorSubject<MenuItems[]>(this.menuItem);
  currentMessage = this.messageSource.asObservable();

  constructor() { }

  changeMessage(message:MenuItems[]){
  this.messageSource.next(message);
}
}
