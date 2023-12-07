import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { KitchenComponent } from './kitchen/kitchen.component';

const routes: Routes = [
    {path: 'kitchen', component: KitchenComponent},
    {path: 'admin', component: KitchenComponent},
    {path: 'statistics', component: KitchenComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
