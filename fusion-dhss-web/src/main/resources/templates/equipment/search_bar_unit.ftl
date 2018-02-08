<#macro search_bar_unit>
<div class="row">
	<div class="col-sm-12">
		<div class="form-inline noPadding">
			<div class="form-group form-group-sm">
				<div id='addBtn' class="btn btn-link btn-md addWrap" >
					<i class="glyphicon glyphicon-plus-sign"></i> 添加
				</div>
			</div>
			<div class="form-group form-group-sm">
				<input id='inputDhssNameTrigger' />
			</div>
			<div class="form-group form-group-sm">
				<input id='inputLocationTrigger' />
			</div>
			<div class="form-group form-group-sm">
				<input id='inputNeTypeTrigger' />
			</div>
			<div class="form-group form-group-sm">
				<input id='inputUnitTypeTrigger' />
			</div>
			<div class="form-group form-group-sm">
				<input id='inputNeTrigger' />
			</div>
			<div class="form-group form-group-sm">
				<input id="inputKeyWord" value=""
					placeholder="筛选单元名称、MML接口地址" style="width: 200px;" title="筛选单元名称、MML接口地址" />
			</div>
			<div style="display:none" class="form-group form-group-sm">
				<input id="numKeyWord" value=""
					placeholder="筛选号码段" style="width: 200px;" title="筛选号码段" />
			</div>
		</div>
	</div>
</div>
</#macro>