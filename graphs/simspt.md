classDiagram
direction BT
class AbstractEntity {
  + AbstractEntity(String) 
  + AbstractEntity() 
  + AbstractEntity(AbstractEntity) 
  + String name
  + int id
  + hashCode() int
  + copy(AbstractEntity) void
  + equals(Object) boolean
  + toString() String
  + clone() AbstractEntity
  + increaseCurrentId(int) void
  + String name
  + int id
}
class AddBrandEvent {
  + AddBrandEvent(double, SpeakerBrand) 
  + AddBrandEvent(AddBrandEvent) 
  + AddBrandEvent() 
  + getEntity(int, double) Entity
  + clone() Event
  + init(State) Set~Integer~
}
class AddDeviceEvent {
  + AddDeviceEvent(double, int, String, SmartDevice) 
  + AddDeviceEvent() 
  + AddDeviceEvent(AddDeviceEvent) 
  + getEntity(int, double) Entity
  + init(State) Set~Integer~
  + clone() Event
}
class AddHouseEvent {
  + AddHouseEvent(double, SmartHouse) 
  + AddHouseEvent(AddHouseEvent) 
  + AddHouseEvent() 
  + clone() Event
  + init(State) Set~Integer~
  + getEntity(int, double) Entity
}
class AddSupplierEvent {
  + AddSupplierEvent() 
  + AddSupplierEvent(double, EnergySupplier) 
  + AddSupplierEvent(AddSupplierEvent) 
  + init(State) Set~Integer~
  + getEntity(int, double) Entity
  + clone() Event
}
class AlreadyExistingEntityException {
  + AlreadyExistingEntityException(String) 
}
class AutoSerializable {
<<Interface>>
  + findField(Class~?~, String) Field?
  + castPrimitive(Object, Class~?~) Object
  + serializeData(Serializer) CharSequence
  + getAllFields(Class~?~) List~Field~
  + deserializeData(Deserializer, String) int
}
class BillingEvent {
  + BillingEvent(double) 
  + BillingEvent() 
  + BillingEvent(BillingEvent) 
  + init(State) Set~Integer~
  + getEntity(int, double) Entity
  + clone() Event
}
class CallMethodEvent {
  + CallMethodEvent(int, double, Method, Object[]) 
  + CallMethodEvent(int, double, Class~?~, String, Object[]) 
  + CallMethodEvent(int, double, String, Object[]) 
  + CallMethodEvent(CallMethodEvent) 
  + CallMethodEvent() 
  + serializeData(Serializer) CharSequence
  + init(State) Set~Integer~
  + deserializeData(Deserializer, String) int
  + getEntity(int, double) Entity
  + clone() CallMethodEvent
}
class Cloneable~T~ {
<<Interface>>
  + clone() T
}
class ConstEntity~T~ {
  + ConstEntity(T) 
  + T obj
  + clone() ConstEntity~T~
  + T obj
}
class Controller {
  + Controller() 
  + Model model
  + View view
  + View view
  + Model model
}
class DeleteEntityEvent {
  + DeleteEntityEvent(double, int) 
  + DeleteEntityEvent() 
  + DeleteEntityEvent(DeleteEntityEvent) 
  + getEntity(int, double) Entity
  + clone() Event
  + init(State) Set~Integer~
}
class DeserializationException {
  + DeserializationException(String, int, CharSequence) 
  + DeserializationException(String, int, DeserializationException, CharSequence) 
  + DeserializationException(String, int, CharSequence, Throwable) 
  + toString() String
  + String preview
  + int position
}
class DeserializationResult~T~ {
  + DeserializationResult(T, int) 
  + consumedLength() int
  + ans() T
}
class Deserializer {
  + Deserializer() 
  + deserializeGenericObj(CharSequence) DeserializationResult~?~
  + deserializeObj(CharSequence) DeserializationResult~AutoSerializable~
  + unescapeChar(char) char
  + unescape(CharSequence) String
  + deserializeBool(CharSequence) DeserializationResult~Boolean~
  + deserializeAuto(CharSequence, Matcher) DeserializationResult~AutoSerializable~
  + deserializeLong(CharSequence) DeserializationResult~Long~
  + wordPattern(String) Pattern
  + deserializeString(CharSequence) DeserializationResult~String~
  + deserializeCollection(CharSequence) DeserializationResult~Collection~?~~
  + deserializeDouble(CharSequence) DeserializationResult~Double~
  + deserializeMap(CharSequence) DeserializationResult~Map~?, ?~~
}
class DummyEvent {
  ~ DummyEvent(double, double) 
  ~ clone() Event
  ~ init(State) Set~Integer~
  ~ getEntity(int, double) Entity
}
class EnergySupplier {
  + EnergySupplier(EnergySupplier) 
  + EnergySupplier() 
  + EnergySupplier(String, double, double, String) 
  + double tax
  + String priceFormula
  + double baseValue
  + clone() EnergySupplier
  + toString() String
  + equals(Object) boolean
  + getBillingVariables(SmartHouse) Map~String, Double~
  + getPrice(Map~String, Double~) double
  + billHouse(SmartHouse, double, double) Receipt
  + copy(AbstractEntity) void
  + String priceFormula
  + double baseValue
  + double tax
}
class EnergySupplierController {
  + EnergySupplierController(Model) 
  + bestSellingSupplier() void
  + listSuppliers() void
  + updateSupplier(Integer, String, Float, Float, String) void
  + getSuppliersByName(String) void
  + addSupplier(String, Float, Float, String) void
  + getSupplierById(Integer) void
  + callViewOnCollection(Collection~EnergySupplier~) void
  + EnergySupplierView view
}
class EnergySupplierView {
  + EnergySupplierView() 
  + show(int, String, double, double, String) void
  + showAll(int, List~Integer~, List~String~, List~Double~, List~Double~, List~String~) void
  + showBest(int, String, double, double, String, Double) void
}
class Entity {
<<Interface>>
  + clone() Entity
  + advanceBy(double) void
}
class EntityController {
  + EntityController(Model) 
  + callViewOnCollection(Collection~AbstractEntity~) void
  + getAll() void
  + get(String) void
  + delete(Integer) void
  + EntityView view
}
class EntityView {
  + EntityView() 
  + showAll(List~Integer~, List~String~, List~String~) void
}
class Event {
  + Event(double) 
  + Event() 
  + Event(double, double) 
  + Event(Event) 
  + double order
  + double time
  + clone() Event
  + getEntity(int, double) Entity
  + init(State) Set~Integer~
  + equals(Object) boolean
  + compareTo(Event) int
  + double order
  + double time
}
class FileController {
  + FileController(Model) 
  + loadSimulationFromFile(String) void
  + saveSimulationToFile(String) void
  + loadScript(String) String
  + sendHelp() void
  + FileView view
}
class FileView {
  + FileView() 
  + loadSuccess() void
  + saveSuccess() void
  + dumpString(String) void
}
class IllegalResolutionException {
  + IllegalResolutionException(String) 
}
class IllegalSupplierValueException {
  + IllegalSupplierValueException(String) 
}
class IllegalVolumeException {
  + IllegalVolumeException(String) 
}
class ImmutableEntity~T~ {
  + ImmutableEntity(T) 
  + T obj
  + clone() ImmutableEntity~T~
  + T obj
}
class Installation {
  + Installation(String, String, double) 
  + price() double
  + deviceName() String
  + room() String
}
class InvalidEntityDeletionException {
  + InvalidEntityDeletionException(String) 
}
class InvalidFormulaException {
  + InvalidFormulaException(String) 
}
class Lamp {
  + Lamp(int) 
  + Lamp() 
  + Lamp(Lamp) 
  + Set(Boolean) void
  + clone() Lamp
  + advanceBy(double) void
}
class LampColour {
<<enumeration>>
  + LampColour() 
  + values() LampColour[]
  + valueOf(String) LampColour
}
class Main {
  + Main() 
  + main(String[]) void
}
class Model {
  + Model() 
  + Simulation simulation
  + double time
  + commitState() void
  + getReceiptsBySupplier(int) List~Receipt~
  + addSimulatorEvent(Event) void
  + bestSellingSupplier() EnergySupplier
  + houseSpentTheMost(double, double) SmartHouse
  + getReceiptsByHouse(int) List~Receipt~
  + updateState() void
  + orderHousesByEnergyConsumption(double, double) List~SmartHouse~
  + copy(Model) void
  + getReceiptsByHouse(int, double, double) List~Receipt~
  + emitReceipts(double) List~Receipt~
  + getReceiptsBySupplier(int, double, double) List~Receipt~
  + Simulation simulation
  + List~Receipt~ lastReceipts
  + double time
}
class MoveDeviceEvent {
  + MoveDeviceEvent(double, int, int, String) 
  + MoveDeviceEvent() 
  + MoveDeviceEvent(MoveDeviceEvent) 
  + clone() Event
  + init(State) Set~Integer~
  + getEntity(int, double) Entity
}
class NoEnergySuppliersExistException {
  + NoEnergySuppliersExistException(String) 
}
class NoHousesExistException {
  + NoHousesExistException(String) 
}
class NoSuchEntityException {
  + NoSuchEntityException(String) 
}
class Receipt {
  + Receipt(Receipt) 
  + Receipt(double, double, EnergySupplier, SmartHouse, List~Installation~, Map~String, Double~, double) 
  + Receipt() 
  + EnergySupplier supplier
  + SmartHouse customer
  + double price
  + double endTime
  + List~Installation~ installations
  + double startTime
  + Map~String, Double~ consumptions
  + equals(Object) boolean
  + clone() Receipt
  + toString() String
  + Map~String, Double~ consumptions
  + double startTime
  + double price
  + List~Installation~ installations
  + EnergySupplier supplier
  + double endTime
  + SmartHouse customer
}
class ReceiptController {
  + ReceiptController(Model) 
  + getReceipts(Double, Double) void
  + getReceiptsbyHouse(Integer) void
  + getReceiptsbySupplier(Integer) void
  + getReceiptsbyHouseTime(Integer, Double) void
  + callViewOnReceiptCollection(List~Receipt~) void
  + ReceiptView view
}
class ReceiptView {
  + ReceiptView() 
  + showReceipt(double, double, int, String, String, int, int, String, double, double, String, double, double) void
  + showReceipts(int, List~Double~, List~Double~, List~Integer~, List~String~, List~String~, List~Integer~, List~Integer~, List~String~, List~Double~, List~Double~, List~String~, List~Double~, List~Double~, List~Double~) void
}
class SerializeIgnore
class Serializer {
  + Serializer() 
  + Serializer(boolean) 
  + boolean indented
  + serializeCollection(Collection~?~) String
  + serializeMap(Map~?, ?~) String
  + serializeLong(Long) String
  + serializeDouble(Double) String
  + serializeGenericObj(Object) String
  + serializeObj(AutoSerializable) String
  + escapeChar(char) String
  + serializeBool(Boolean) String
  + escape(CharSequence) String
  + serializeString(String) String
  + boolean indented
}
class SetEntityEvent {
  + SetEntityEvent() 
  + SetEntityEvent(int, double, Entity) 
  + SetEntityEvent(SetEntityEvent) 
  + init(State) Set~Integer~
  + getEntity(int, double) Entity
  + clone() SetEntityEvent
}
class SetStateEvent {
  + SetStateEvent() 
  + SetStateEvent(SetStateEvent) 
  + SetStateEvent(SimState, double) 
  + deserializeData(Deserializer, String) int
  + serializeData(Serializer) CharSequence
  + getEntity(int, double) Entity
  + clone() SetStateEvent
  + init(State) Set~Integer~
}
class SimEntity {
  + index() int
}
class SimState {
<<Interface>>
  + getEntity(int) Entity
  + Set~Integer~ validIds
}
class Simulation {
  + Simulation() 
  + Simulation(State) 
  + Simulation(Simulation) 
  + getEntityWithType(Class~T~, int) T
  + clone() Simulation
  + addSupplier(EnergySupplier) void
  + deleteHouse(int) void
  + getEntitiesByName(String) Set~AbstractEntity~
  + getDevice(Class~T~, int) T
  + changeHouseSupplier(int, EnergySupplier) void
  + getSpeakerBrand(String) SpeakerBrand
  + getEntityByType(Class~T~) Set~T~
  + getDevicesInHouse(Class~T~, int) Set~T~
  + deleteSpeakerBrand(String) void
  + updateDevice(int, SmartDevice) void
  + addSpeakerBrand(SpeakerBrand) void
  + equals(Object) boolean
  + updateSupplier(int, EnergySupplier) void
  + resetCounters() void
  + toString() String
  + deleteDevice(int) void
  + updateSpeakerBrand(String, SpeakerBrand) void
  + addHouse(SmartHouse) void
  + updateHouse(int, SmartHouse) void
  + deleteEntity(int) void
  + addDevice(int, String, SmartDevice) void
  + getDevicesByRoom(int) Map~String, Set~SmartDevice~~
  + getEntity(int) AbstractEntity
  + Set~SpeakerBrand~ allSpeakerBrands
  + Set~EnergySupplier~ suppliers
  + Map~List~String~, Set~SmartDevice~~ devicesByRoom
  + Set~Integer~ validIds
  + Set~SmartHouse~ houses
}
class SimulationController {
  + SimulationController(Model) 
  + addMoment(Double) void
  + getMoment() void
  + addMomentSpecified(Integer, Double) void
  + Double moment
  + SimulationView view
}
class SimulationView {
  + SimulationView() 
  + successSet() void
  + successAdd() void
  + successGet(Double) void
}
class Simulator {
  + Simulator(SimState) 
  + Simulator() 
  + Simulator(SimState, double) 
  + removeAllEvents() void
  + getStateAs(Class~T~, double) T
  + removeEvents(Predicate~Event~) int
  + deserializeData(Deserializer, String) int
  + getState(double) State
  + addEvent(Event) void
  + removeEvent(Event) boolean
  + serializeData(Serializer) CharSequence
}
class SmartBulb {
  + SmartBulb(boolean, String, float, LampColour) 
  + SmartBulb() 
  + SmartBulb(SmartBulb) 
  + LampColour colour
  + float dimension
  + energyOutput() double
  + deserializeData(Deserializer, String) int
  + copy(AbstractEntity) void
  + getColourOutput(LampColour) double
  + serializeData(Serializer) CharSequence
  + toString() String
  + installationPrice() double
  + equals(Object) boolean
  + clone() SmartBulb
  + LampColour colour
  + float dimension
}
class SmartBulbController {
  + SmartBulbController(Model) 
  + getBulb(Integer) void
  + getBulbInHouse(Integer) void
  + colourFromString(String) LampColour
  + callViewOnCollection(Collection~SmartBulb~) void
  + addBulb(Integer, String, String, Boolean, String, Float) void
  + updateBulb(Integer, String, Boolean, String, Float) void
  + SmartBulbView view
}
class SmartBulbView {
  + SmartBulbView() 
  + show(int, String, boolean, String, float) void
  + showAll(int, List~Integer~, List~String~, List~Boolean~, List~String~, List~Float~) void
}
class SmartCamera {
  + SmartCamera() 
  + SmartCamera(SmartCamera) 
  + SmartCamera(boolean, String, int[], long) 
  + long fileResolution
  + int[] cameraResolution
  + energyOutput() double
  + installationPrice() double
  + toString() String
  + clone() SmartCamera
  + int[] cameraResolution
  + long fileResolution
}
class SmartCameraController {
  + SmartCameraController(Model) 
  + getCameraById(Integer) void
  + cr2String(int[]) String
  + callViewOnCollection(Collection~SmartCamera~) void
  + getCamerasByHouse(Integer) void
  + updateCamera(Integer, String, Boolean, Integer, Integer, Integer) void
  + addCamera(Integer, String, String, Boolean, Integer, Integer, Long) void
  + SmartCameraView view
}
class SmartCameraView {
  + SmartCameraView() 
  + showAll(int, List~Integer~, List~String~, List~Boolean~, List~String~, List~Long~) void
  + show(int, String, boolean, String, long) void
}
class SmartDevice {
  + SmartDevice(SmartDevice) 
  + SmartDevice() 
  + SmartDevice(String) 
  + SmartDevice(boolean, String) 
  + boolean on
  + double energyCounter
  + installationPrice() double
  + advanceBy(double) void
  + resetEnergyCounter() void
  + energyOutput() double
  + equals(Object) boolean
  + copy(AbstractEntity) void
  + toString() String
  + clone() SmartDevice
  + boolean on
  + double energyCounter
}
class SmartDeviceController {
  + SmartDeviceController(Model) 
  + getDevices() void
  + moveDevice(Integer, Integer, String) void
  + turnAllDevices(Integer, Boolean) void
  + turnAllDevices(Boolean) void
  + callViewOnCollection(Map~List~String~, Set~SmartDevice~~) void
  + turnAllDevices(Integer, String, Boolean) void
  + turnDevice(Integer, Boolean) void
  + SmartDeviceView view
}
class SmartDeviceView {
  + SmartDeviceView() 
  + showAll(List~Integer~, List~String~, List~Boolean~, List~String~, List~String~) void
}
class SmartHouse {
  + SmartHouse(SmartHouse) 
  + SmartHouse(String, String, int, int) 
  + SmartHouse() 
  + Map~String, Set~SmartDevice~~ installations
  + Map~String, Set~Integer~~ rooms
  + String ownerName
  + Map~Integer, SmartDevice~ devices
  + int ownerNif
  + int supplierId
  + equals(Object) boolean
  + switchById(int, boolean) void
  + moveToRoom(int, String) void
  + removeDevice(SmartDevice) void
  + containsDevice(int) boolean
  + resetCounters() void
  + switchAllInRoom(String, boolean) void
  + switchAll(boolean) void
  + updateDevice(int, SmartDevice) void
  + copy(AbstractEntity) void
  + toString() String
  + clone() SmartHouse
  + addDevice(SmartDevice, String) void
  + advanceBy(double) void
  + Map~Integer, SmartDevice~ devices
  + int supplierId
  + int ownerNif
  + String ownerName
  + Map~String, Set~SmartDevice~~ installations
  + Map~String, Set~Integer~~ rooms
  + Map~String, Set~SmartDevice~~ devicesByRoom
  + Map~String, Double~ devicesConsumptions
}
class SmartHouse {
  + SmartHouse() 
  + SmartHouse(String, Lamp, Lamp, Lamp) 
  + getEntity(int) Entity
  + deserializeData(Deserializer, String) int
  + SetAddress(ImmutableEntity~String~) void
  + consumoTotal() double
  + serializeData(Serializer) CharSequence
  + Set~Integer~ validIds
}
class SmartHouseController {
  + SmartHouseController(Model) 
  + changeHouseSupplier(Integer, Integer) void
  + getHouseWhichSpentMost(Double, Double) void
  + updateHouse(Integer, String, Integer, String, Integer) void
  + deleteHouse(Integer) void
  + getHouseById(Integer) void
  + getHouseByEnergyConsumptionDouble(Double, Double) void
  + callViewOnCollection(Collection~SmartHouse~) void
  + callViewOnCollection(List~SmartHouse~, Map~Integer, Double~) void
  + addHouse(String, Integer, String, Integer) void
  + getDevices() void
  + SmartHouseView view
}
class SmartHouseView {
  + SmartHouseView() 
  + showMostSpentHouse(int, String, String, Integer, Integer, String, Double, Double, String, Map~String, Set~Integer~~, Double) void
  + showHouses(int, List~Integer~, List~String~, List~String~, List~Integer~, List~Integer~, List~String~, List~Double~, List~Double~, List~String~, List~Map~String, Set~Integer~~~) void
  + showHouse(int, String, String, Integer, Integer, String, Double, Double, String, Map~String, Set~Integer~~) void
  + showHousesConsumption(int, List~Integer~, List~String~, List~String~, List~Integer~, List~Integer~, List~String~, List~Double~, List~Double~, List~String~, List~Map~String, Set~Integer~~~, List~Double~) void
}
class SmartSpeaker {
  + SmartSpeaker(String, SpeakerBrand) 
  + SmartSpeaker(SpeakerBrand) 
  + SmartSpeaker(SmartSpeaker) 
  + SmartSpeaker() 
  + SmartSpeaker(boolean, String, int, String, SpeakerBrand) 
  + String radioStation
  + int volume
  + SpeakerBrand brand
  + toString() String
  + clone() SmartDevice
  + energyOutput() double
  + installationPrice() double
  + equals(Object) boolean
  + SpeakerBrand brand
  + int volume
  + String radioStation
}
class SmartSpeakerController {
  + SmartSpeakerController(Model) 
  + getSpeakerById(Integer) void
  + callViewOnCollection(Collection~SmartSpeaker~) void
  + updateBrand(String, String, Integer) void
  + getSpeakersByHouse(Integer) void
  + addSpeaker(Integer, String, String, Boolean, String, String, Integer) void
  + getBrands() void
  + updateSpeaker(Integer, String, Boolean, String, String, Integer) void
  + addBrand(String, Integer) void
  + SmartSpeakerView view
}
class SmartSpeakerView {
  + SmartSpeakerView() 
  + show(int, String, boolean, String, int, int, String) void
  + showBrand(String, int) void
  + showAll(int, List~Integer~, List~String~, List~Boolean~, List~String~, List~Integer~, List~Integer~, List~String~) void
  + showBrands(int, List~Integer~, List~String~, List~Integer~) void
}
class SpeakerBrand {
  + SpeakerBrand() 
  + SpeakerBrand(String, int) 
  + SpeakerBrand(SpeakerBrand) 
  + int dailyConsumption
  + clone() SpeakerBrand
  + equals(Object) boolean
  + toString() String
  + hashCode() int
  + installationPrice() double
  + int dailyConsumption
}
class State {
  + State(double) 
  + Set~Integer~ validIds
  + getEntity(int) Entity
  + Set~Integer~ validIds
}
class SwitchAllEvent {
  + SwitchAllEvent(double, boolean) 
  + SwitchAllEvent(SwitchAllEvent) 
  + SwitchAllEvent() 
  + init(State) Set~Integer~
  + clone() Event
  + getEntity(int, double) Entity
}
class SwitchDeviceEvent {
  + SwitchDeviceEvent(double, int, boolean) 
  + SwitchDeviceEvent() 
  + SwitchDeviceEvent(SwitchDeviceEvent) 
  + clone() Event
  + init(State) Set~Integer~
  + getEntity(int, double) Entity
}
class SwitchHouseEvent {
  + SwitchHouseEvent(double, int, boolean) 
  + SwitchHouseEvent(SwitchHouseEvent) 
  + SwitchHouseEvent() 
  + getEntity(int, double) Entity
  + clone() Event
  + init(State) Set~Integer~
}
class SwitchRoomEvent {
  + SwitchRoomEvent() 
  + SwitchRoomEvent(SwitchRoomEvent) 
  + SwitchRoomEvent(double, int, String, boolean) 
  + init(State) Set~Integer~
  + getEntity(int, double) Entity
  + clone() Event
}
class TablePrinter {
  + TablePrinter() 
  + printEdgeLine(int) void
  + tablePrint(int, List~String~, List~?~[]) void
  + getRawStrings(int, int, List~?~[]) List~List~String~~
  + printLine(List~String~, List~Integer~) void
  + getColumnLengths(List~String~, List~?~[]) List~Integer~
}
class Teste {
  + Teste() 
  + main(String[]) void
}
class ToggleLampEvent {
  + ToggleLampEvent(ToggleLampEvent) 
  + ToggleLampEvent(int, double) 
  + ToggleLampEvent() 
  + init(State) Set~Integer~
  + clone() ToggleLampEvent
  + getEntity(int, double) Entity
}
class UpdateBrandEvent {
  + UpdateBrandEvent(double, String, SpeakerBrand) 
  + UpdateBrandEvent() 
  + UpdateBrandEvent(UpdateBrandEvent) 
  + getEntity(int, double) Entity
  + clone() Event
  + init(State) Set~Integer~
}
class UpdateDeviceEvent {
  + UpdateDeviceEvent(double, int, SmartDevice) 
  + UpdateDeviceEvent() 
  + UpdateDeviceEvent(UpdateDeviceEvent) 
  + init(State) Set~Integer~
  + clone() Event
  + getEntity(int, double) Entity
}
class UpdateHouseEvent {
  + UpdateHouseEvent(UpdateHouseEvent) 
  + UpdateHouseEvent() 
  + UpdateHouseEvent(double, int, String, int, String, int) 
  + UpdateHouseEvent(double, int, int) 
  + init(State) Set~Integer~
  + clone() Event
  + getEntity(int, double) Entity
}
class UpdateSupplierEvent {
  + UpdateSupplierEvent(double, int, String, float, float, String) 
  + UpdateSupplierEvent(UpdateSupplierEvent) 
  + UpdateSupplierEvent() 
  + clone() Event
  + init(State) Set~Integer~
  + getEntity(int, double) Entity
}
class View {
  + View() 
  + error(String) void
  + warning(String) void
}
class WrongEntityTypeException {
  + WrongEntityTypeException(String) 
}
class WrongSupplierException {
  + WrongSupplierException(String) 
}

AbstractEntity  ..>  AutoSerializable 
AbstractEntity  ..>  Entity 
AbstractEntity  ..>  WrongEntityTypeException : «create»
AddBrandEvent  -->  Event 
AddBrandEvent "1" *--> "brand 1" SpeakerBrand 
AddDeviceEvent  -->  Event 
AddDeviceEvent "1" *--> "device 1" SmartDevice 
AddDeviceEvent "1" *--> "house 1" SmartHouse 
AddHouseEvent  -->  Event 
AddHouseEvent "1" *--> "house 1" SmartHouse 
AddSupplierEvent "1" *--> "supplier 1" EnergySupplier 
AddSupplierEvent  -->  Event 
AutoSerializable  ..>  DeserializationException : «create»
BillingEvent  -->  Event 
BillingEvent  ..>  Simulation : «create»
BillingEvent "1" *--> "state 1" Simulation 
CallMethodEvent "1" *--> "entity 1" Entity 
CallMethodEvent  -->  Event 
ConstEntity~T~  ..>  Cloneable~T~ 
ConstEntity~T~  ..>  Entity 
Controller "1" *--> "model 1" Model 
Controller "1" *--> "view 1" View 
DeleteEntityEvent "1" *--> "target 1" Entity 
DeleteEntityEvent  -->  Event 
DeleteEntityEvent  ..>  Simulation : «create»
Deserializer "1" *--> "deserializedObjects *" AutoSerializable 
Deserializer  ..>  DeserializationException : «create»
Deserializer  ..>  DeserializationResult~T~ : «create»
DummyEvent  -->  Event 
EnergySupplier  -->  AbstractEntity 
EnergySupplier  ..>  AutoSerializable 
EnergySupplier  ..>  IllegalSupplierValueException : «create»
EnergySupplier  ..>  Installation : «create»
EnergySupplier  ..>  InvalidFormulaException : «create»
EnergySupplier  ..>  Receipt : «create»
EnergySupplier  ..>  WrongEntityTypeException : «create»
EnergySupplier  ..>  WrongSupplierException : «create»
EnergySupplierController  ..>  AddSupplierEvent : «create»
EnergySupplierController  -->  Controller 
EnergySupplierController  ..>  EnergySupplier : «create»
EnergySupplierController  ..>  EnergySupplierView : «create»
EnergySupplierController  ..>  UpdateSupplierEvent : «create»
EnergySupplierView  -->  View 
EntityController  -->  Controller 
EntityController  ..>  DeleteEntityEvent : «create»
EntityController  ..>  EntityView : «create»
EntityView  -->  View 
Event  ..>  AutoSerializable 
FileController  -->  Controller 
FileController  ..>  FileView : «create»
FileView  -->  View 
ImmutableEntity~T~  ..>  Entity 
Lamp  ..>  AutoSerializable 
Lamp  ..>  Entity 
SmartBulb  -->  LampColour 
Main  ..>  Model : «create»
Model  ..>  BillingEvent : «create»
Model  ..>  NoEnergySuppliersExistException : «create»
Model  ..>  NoHousesExistException : «create»
Model  ..>  SetEntityEvent : «create»
Model  ..>  Simulation : «create»
Model "1" *--> "simulation 1" Simulation 
Model "1" *--> "simulator 1" Simulator 
Model  ..>  Simulator : «create»
MoveDeviceEvent  -->  Event 
MoveDeviceEvent  ..>  Simulation : «create»
MoveDeviceEvent "1" *--> "oldHouse 1" SmartHouse 
Receipt  ..>  AutoSerializable 
Receipt "1" *--> "supplier 1" EnergySupplier 
Receipt "1" *--> "installations *" Installation 
Receipt "1" *--> "customer 1" SmartHouse 
ReceiptController  -->  Controller 
ReceiptController  ..>  ReceiptView : «create»
ReceiptView  -->  View 
Serializer "1" *--> "serializedObjects *" AutoSerializable 
SetEntityEvent "1" *--> "entity 1" Entity 
SetEntityEvent  -->  Event 
SetStateEvent "1" *--> "entities *" Entity 
SetStateEvent  -->  Event 
Simulation "1" *--> "entities *" AbstractEntity 
Simulation  ..>  AlreadyExistingEntityException : «create»
Simulation  ..>  AutoSerializable 
Simulation  ..>  EnergySupplier : «create»
Simulation  ..>  InvalidEntityDeletionException : «create»
Simulation  ..>  NoSuchEntityException : «create»
Simulation  ..>  SimState 
Simulation  ..>  SmartHouse : «create»
Simulation "1" *--> "speakerBrands *" SpeakerBrand 
Simulation  ..>  WrongEntityTypeException : «create»
SimulationController  -->  Controller 
SimulationController  ..>  SimulationView : «create»
SimulationView  -->  View 
Simulator  ..>  AutoSerializable 
Simulator  ..>  DummyEvent : «create»
Simulator "1" *--> "eventSet *" Event 
Simulator  ..>  SetStateEvent : «create»
Simulator  ..>  State : «create»
SmartBulb  ..>  AutoSerializable 
SmartBulb  ..>  DeserializationException : «create»
SmartBulb "1" *--> "colour 1" LampColour 
SmartBulb  -->  SmartDevice 
SmartBulb  ..>  WrongEntityTypeException : «create»
SmartBulbController  ..>  AddDeviceEvent : «create»
SmartBulbController  -->  Controller 
SmartBulbController  ..>  SmartBulb : «create»
SmartBulbController  ..>  SmartBulbView : «create»
SmartBulbController  ..>  UpdateDeviceEvent : «create»
SmartBulbView  -->  View 
SmartCamera  ..>  AutoSerializable 
SmartCamera  ..>  IllegalResolutionException : «create»
SmartCamera  -->  SmartDevice 
SmartCameraController  ..>  AddDeviceEvent : «create»
SmartCameraController  -->  Controller 
SmartCameraController  ..>  SmartCamera : «create»
SmartCameraController  ..>  SmartCameraView : «create»
SmartCameraController  ..>  UpdateDeviceEvent : «create»
SmartCameraView  -->  View 
SmartDevice  -->  AbstractEntity 
SmartDevice  ..>  AutoSerializable 
SmartDevice  ..>  WrongEntityTypeException : «create»
SmartDeviceController  -->  Controller 
SmartDeviceController  ..>  MoveDeviceEvent : «create»
SmartDeviceController  ..>  SmartDeviceView : «create»
SmartDeviceController  ..>  SwitchAllEvent : «create»
SmartDeviceController  ..>  SwitchDeviceEvent : «create»
SmartDeviceController  ..>  SwitchHouseEvent : «create»
SmartDeviceController  ..>  SwitchRoomEvent : «create»
SmartDeviceView  -->  View 
SmartHouse  -->  AbstractEntity 
SmartHouse  ..>  AutoSerializable 
SmartHouse  ..>  AutoSerializable 
SmartHouse  ..>  ImmutableEntity~T~ : «create»
SmartHouse "1" *--> "l1 1" Lamp 
SmartHouse  ..>  NoSuchEntityException : «create»
SmartHouse  ..>  SimState 
SmartHouse "1" *--> "devices *" SmartDevice 
SmartHouse  ..>  WrongEntityTypeException : «create»
SmartHouseController  ..>  AddHouseEvent : «create»
SmartHouseController  -->  Controller 
SmartHouseController  ..>  DeleteEntityEvent : «create»
SmartHouseController  ..>  SmartHouse : «create»
SmartHouseController  ..>  SmartHouseView : «create»
SmartHouseController  ..>  UpdateHouseEvent : «create»
SmartHouseView  -->  View 
SmartSpeaker  ..>  AutoSerializable 
SmartSpeaker  ..>  IllegalVolumeException : «create»
SmartSpeaker  -->  SmartDevice 
SmartSpeaker "1" *--> "brand 1" SpeakerBrand 
SmartSpeaker  ..>  SpeakerBrand : «create»
SmartSpeakerController  ..>  AddBrandEvent : «create»
SmartSpeakerController  ..>  AddDeviceEvent : «create»
SmartSpeakerController  -->  Controller 
SmartSpeakerController  ..>  SmartSpeaker : «create»
SmartSpeakerController  ..>  SmartSpeakerView : «create»
SmartSpeakerController  ..>  SpeakerBrand : «create»
SmartSpeakerController  ..>  UpdateBrandEvent : «create»
SmartSpeakerController  ..>  UpdateDeviceEvent : «create»
SmartSpeakerView  -->  View 
SpeakerBrand  -->  AbstractEntity 
SpeakerBrand  ..>  AutoSerializable 
State  ..>  DummyEvent : «create»
State  ..>  SimState 
Simulator  -->  State 
SwitchAllEvent  -->  Event 
SwitchAllEvent "1" *--> "state 1" Simulation 
SwitchAllEvent  ..>  Simulation : «create»
SwitchDeviceEvent  -->  Event 
SwitchDeviceEvent  ..>  Simulation : «create»
SwitchDeviceEvent "1" *--> "house 1" SmartHouse 
SwitchHouseEvent  -->  Event 
SwitchHouseEvent "1" *--> "house 1" SmartHouse 
SwitchRoomEvent  -->  Event 
SwitchRoomEvent "1" *--> "house 1" SmartHouse 
Teste  ..>  CallMethodEvent : «create»
Teste  ..>  Deserializer : «create»
Teste  ..>  Lamp : «create»
Teste  ..>  Serializer : «create»
Teste  ..>  Simulator : «create»
Teste  ..>  SmartHouse : «create»
Teste  ..>  ToggleLampEvent : «create»
ToggleLampEvent  -->  Event 
ToggleLampEvent "1" *--> "new_lamp 1" Lamp 
UpdateBrandEvent  -->  Event 
UpdateBrandEvent "1" *--> "brand 1" SpeakerBrand 
UpdateDeviceEvent  -->  Event 
UpdateDeviceEvent  ..>  Simulation : «create»
UpdateDeviceEvent "1" *--> "device 1" SmartDevice 
UpdateDeviceEvent "1" *--> "house 1" SmartHouse 
UpdateHouseEvent  -->  Event 
UpdateHouseEvent "1" *--> "house 1" SmartHouse 
UpdateSupplierEvent "1" *--> "supplier 1" EnergySupplier 
UpdateSupplierEvent  -->  Event 
