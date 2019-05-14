import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPedido, Pedido } from 'app/shared/model/pedido.model';
import { PedidoService } from './pedido.service';
import { ICliente } from 'app/shared/model/cliente.model';
import { ClienteService } from 'app/entities/cliente';

@Component({
    selector: 'jhi-pedido-update',
    templateUrl: './pedido-update.component.html'
})
export class PedidoUpdateComponent implements OnInit {
    pedido: IPedido;
    isSaving: boolean;

    clientes: ICliente[];

    editForm = this.fb.group({
        id: [],
        nombre: [null, [Validators.required]],
        descripcion: [],
        cliente: []
    });

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected pedidoService: PedidoService,
        protected clienteService: ClienteService,
        protected activatedRoute: ActivatedRoute,
        private fb: FormBuilder
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ pedido }) => {
            this.updateForm(pedido);
            this.pedido = pedido;
        });
        this.clienteService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICliente[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICliente[]>) => response.body)
            )
            .subscribe((res: ICliente[]) => (this.clientes = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    updateForm(pedido: IPedido) {
        this.editForm.patchValue({
            id: pedido.id,
            nombre: pedido.nombre,
            descripcion: pedido.descripcion,
            cliente: pedido.cliente
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        const pedido = this.createFromForm();
        if (pedido.id !== undefined) {
            this.subscribeToSaveResponse(this.pedidoService.update(pedido));
        } else {
            this.subscribeToSaveResponse(this.pedidoService.create(pedido));
        }
    }

    private createFromForm(): IPedido {
        const entity = {
            ...new Pedido(),
            id: this.editForm.get(['id']).value,
            nombre: this.editForm.get(['nombre']).value,
            descripcion: this.editForm.get(['descripcion']).value,
            cliente: this.editForm.get(['cliente']).value
        };
        return entity;
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IPedido>>) {
        result.subscribe((res: HttpResponse<IPedido>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackClienteById(index: number, item: ICliente) {
        return item.id;
    }
}
