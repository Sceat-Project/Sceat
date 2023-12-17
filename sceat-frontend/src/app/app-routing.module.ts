import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { KitchenComponent } from './kitchen/kitchen.component';
import { SchoolAdministrativeComponent } from './school-administrative/school-administrative.component';
import { StatisticsViewComponent } from './statistics-view/statistics-view.component';
import { ItemManagerComponent } from './school-administrative/item-manager/item-manager.component';
import {StudentreaderComponent} from "./kitchen/student-reader/studentreader/studentreader.component";

const routes: Routes = [
    {path: 'kitchen', component: KitchenComponent},
    {path: 'admin', component: SchoolAdministrativeComponent},
    {path: 'statistics', component: StatisticsViewComponent},
    {path: 'item-manager', component: ItemManagerComponent},
    {path: 'student-reader', component: StudentreaderComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
