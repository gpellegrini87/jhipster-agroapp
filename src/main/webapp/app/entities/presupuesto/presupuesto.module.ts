import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { AgroappSharedModule } from 'app/shared';
import {
    PresupuestoComponent,
    PresupuestoDetailComponent,
    PresupuestoUpdateComponent,
    PresupuestoDeletePopupComponent,
    PresupuestoDeleteDialogComponent,
    presupuestoRoute,
    presupuestoPopupRoute
} from './';

const ENTITY_STATES = [...presupuestoRoute, ...presupuestoPopupRoute];

@NgModule({
    imports: [AgroappSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PresupuestoComponent,
        PresupuestoDetailComponent,
        PresupuestoUpdateComponent,
        PresupuestoDeleteDialogComponent,
        PresupuestoDeletePopupComponent
    ],
    entryComponents: [PresupuestoComponent, PresupuestoUpdateComponent, PresupuestoDeleteDialogComponent, PresupuestoDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgroappPresupuestoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
