import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { AgroappProveedorModule } from './proveedor/proveedor.module';
import { AgroappClienteModule } from './cliente/cliente.module';
import { AgroappPedidoModule } from './pedido/pedido.module';
import { AgroappPresupuestoModule } from './presupuesto/presupuesto.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        AgroappProveedorModule,
        AgroappClienteModule,
        AgroappPedidoModule,
        AgroappPresupuestoModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgroappEntityModule {}
