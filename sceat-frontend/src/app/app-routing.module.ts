import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { KitchenComponent } from './kitchen/kitchen.component';
import { SchoolAdministrativeComponent } from './school-administrative/school-administrative.component';
import { StatisticsViewComponent } from './statistics-view/statistics-view.component';

const routes: Routes = [
    {path: 'kitchen', component: KitchenComponent},
    {path: 'admin', component: SchoolAdministrativeComponent},
    {path: 'statistics', component: StatisticsViewComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
